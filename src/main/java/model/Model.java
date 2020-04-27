package model;

import com.digitalpetri.modbus.codec.Modbus;
import com.digitalpetri.modbus.master.ModbusTcpMaster;
import com.digitalpetri.modbus.master.ModbusTcpMasterConfig;
import com.digitalpetri.modbus.requests.ReadHoldingRegistersRequest;
import com.digitalpetri.modbus.requests.WriteSingleRegisterRequest;
import com.digitalpetri.modbus.responses.ReadHoldingRegistersResponse;
import com.digitalpetri.modbus.responses.WriteSingleRegisterResponse;
import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Model {

    private static final Logger logger = LogManager.getLogger(Model.class);
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    static int WEIGHT_REGISTER = 0x2002;
    static int ZERO_REGISTER = 0x2061;
    static int SET_ZERO_COMMAND = 0x02;

    private Map<MasterInfo, ModbusTcpMaster> masters = new HashMap<>();
    private Map<MasterInfo, Integer> masterConnectRetryCount = new HashMap<>();

    Configuration configuration;
    ModbusTcpMaster master;

    boolean isActive = false;
    Consumer<DisplayMessage> weightConsumer;
    Consumer<ZeroCommand> zeroCommandConsumer;

    public Model(Configuration configuration, Consumer<DisplayMessage> weightConsumer) {
        this.configuration = configuration;
        this.weightConsumer = weightConsumer;
    }

    public void addMaster(MasterInfo masterInfo) {
        ModbusTcpMasterConfig config = new ModbusTcpMasterConfig
                .Builder(masterInfo.getIpAddress())
                .setPort(masterInfo.getPort())
                .setTimeout(Duration.ofSeconds(1))
                .build();

        sendError(masterInfo, configuration.getConnectingMessage());

        master = new ModbusTcpMaster(config);
        CompletableFuture<ModbusTcpMaster> future = master.connect();

        final int masterConnectRetryCount = getMasterConnectRetryCount(masterInfo);

        future.whenCompleteAsync((master, ex) -> {
            if(master == null) {
                scheduler.schedule(() -> addMaster(masterInfo), configuration.getConnectRetryTimeoutSec(), TimeUnit.SECONDS);
                char[] dots = new char[masterConnectRetryCount % 5];
                Arrays.fill(dots, '.');
                String errorMsg = configuration.getNoConnectMsg() +  " " + new String(dots);
                sendError(masterInfo, errorMsg);
                logger.error("Cannot connect to master " + masterInfo.toString());
            } else {
                masters.put(masterInfo, master);
                isActive = true;
                pollWeight(masterInfo);
            }
        });
    }


    private int getMasterConnectRetryCount(MasterInfo masterInfo) {
        Integer retryCounter = masterConnectRetryCount.get(masterInfo);
        if(retryCounter == null) {
            retryCounter = new Integer(0);
            masterConnectRetryCount.put(masterInfo, retryCounter);
            return 0;
        } else {
            Integer inc = new Integer(retryCounter.intValue() + 1);
            masterConnectRetryCount.put(masterInfo, new Integer(inc));
            return inc;
        }
    }

    private void sendWeightInfo(MasterInfo masterInfo, float weight) {
        weightConsumer.accept(new DisplayWeightMessage(masterInfo, weight));
    }

    private void sendError(MasterInfo masterInfo, String errorMsg) {
        weightConsumer.accept(new DisplayErrorMessage(masterInfo, errorMsg));
    }

    private void pollWeight(MasterInfo masterInfo) {

        if(!isActive) {
            return;
        }

        ModbusTcpMaster master = masters.get(masterInfo);
        if(master == null) {
            addMaster(masterInfo);
            return;
        }

        CompletableFuture<ReadHoldingRegistersResponse> future =
                master.sendRequest(new ReadHoldingRegistersRequest(WEIGHT_REGISTER, 2), 0);

        future.whenCompleteAsync((response, ex) -> {
            if (response != null) {
                ByteBuf byteBuffer = response.getRegisters();
                Float weight = byteBuffer.getFloat(0);
                sendWeightInfo(masterInfo, weight);
                ReferenceCountUtil.release(response);
            } else {
                logger.error("Completed exceptionally, message={}", ex.getMessage(), ex);
                masters.remove(masterInfo);
                addMaster(masterInfo);
            }
            scheduler.schedule(() -> pollWeight(masterInfo), configuration.getPollPeriodMs(), TimeUnit.MILLISECONDS);
        }, Modbus.sharedExecutor());
    }

    private void sendZeroCommand(ZeroCommand zeroCommand) {

        if(!isActive) {
            return;
        }

        ModbusTcpMaster master = masters.get(zeroCommand.getMasterInfo());
        if(master == null) {
            addMaster(zeroCommand.getMasterInfo());
            return;
        }

        CompletableFuture<WriteSingleRegisterResponse> future =
                master.sendRequest(new WriteSingleRegisterRequest(ZERO_REGISTER, SET_ZERO_COMMAND), 0);

        future.whenCompleteAsync((response, ex) -> {
            if (response != null) {
                int code = response.getFunctionCode().getCode();
                ReferenceCountUtil.release(response);
                System.out.println("got response to zero: " + response.toString());
            } else {
                logger.error("Completed exceptionally, message={}", ex.getMessage(), ex);
                masters.remove(zeroCommand.getMasterInfo());
                addMaster(zeroCommand.getMasterInfo());
            }
        }, Modbus.sharedExecutor());

    }

    public void onExit() {
        isActive = false;
        scheduler.shutdown();
        master.disconnect();
    }

    public Consumer<ZeroCommand> getZeroCommandConsumer() {
        this.zeroCommandConsumer = zc -> sendZeroCommand(zc);
        return zeroCommandConsumer;
    }
}
