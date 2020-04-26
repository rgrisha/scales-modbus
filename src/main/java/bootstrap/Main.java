package bootstrap;

import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
import gui.UserInterface;
import gui.UserInterfaceSwing;
import model.Configuration;
import model.MasterInfo;
import model.Model;
import model.WeightInfo;

import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {

        Configuration configuration = new Configuration();
        configuration.setPollPeriodMs(500);
        configuration.setConnectRetryTimeoutSec(10);
        configuration.setSetZeroText("Nust. 0");
        configuration.setConnectingMessage("Jungiamasi");

        MasterInfo masterInfo = new MasterInfo();
        masterInfo.setName("rus");
        masterInfo.setIpAddress("192.168.1.100");
        masterInfo.setPort(502);
        configuration.addMasterConfiguration(masterInfo);

        masterInfo = new MasterInfo();
        masterInfo.setName("rus");
        masterInfo.setName("euro");
        masterInfo.setIpAddress("192.168.1.101");
        masterInfo.setPort(502);
        configuration.addMasterConfiguration(masterInfo);

        UserInterface ui = UserInterfaceSwing.newInterface(configuration);
        Consumer<WeightInfo> weightConsumer = ui.getWeightConsumer();
        Model model = new Model(configuration, weightConsumer);

        for(int i = 0; i < configuration.getMasterCount(); i++) {
            ui.addMaster(configuration.getMasterConfiguration(i));
            model.addMaster(configuration.getMasterConfiguration(i));
        }

        ui.setEventListenerCommandZero(model.getZeroCommandConsumer());

    }
}
