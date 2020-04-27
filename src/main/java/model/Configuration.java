package model;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

    @ConfigProperty
    private int pollPeriodMs = 300;

    @ConfigProperty
    private int connectRetryTimeoutSec = 6;

    @ConfigProperty
    private String setZeroText = "Nust. 0";

    @ConfigProperty
    private String connectingMessage = "Jungiamasi";

    private List<MasterInfo> masterConfigs = new ArrayList<>();

    public int getPollPeriodMs() {
        return pollPeriodMs;
    }

    public void setPollPeriodMs(int pollPeriodMs) {
        this.pollPeriodMs = pollPeriodMs;
    }

    public void setPollPeriodMs(String val) {
        this.pollPeriodMs = Integer.parseInt(val);
    }

    public int getConnectRetryTimeoutSec() {
        return connectRetryTimeoutSec;
    }

    public void setConnectRetryTimeoutSec(int connectRetryTimeoutSec) {
        this.connectRetryTimeoutSec = connectRetryTimeoutSec;
    }

    public void setConnectRetryTimeoutSec(String val) {
        this.connectRetryTimeoutSec = Integer.parseInt(val);
    }

    public String getSetZeroText() {
        return setZeroText;
    }

    public void setSetZeroText(String setZeroText) {
        this.setZeroText = setZeroText;
    }

    public String getConnectingMessage() {
        return connectingMessage;
    }

    public void setConnectingMessage(String connectingMessage) {
        this.connectingMessage = connectingMessage;
    }

    public void addMasterConfiguration(MasterInfo mc) {
        this.masterConfigs.add(mc);
    }

    public MasterInfo getMasterConfiguration(int masterNumber) {
        return this.masterConfigs.get(masterNumber);
    }

    public int getMasterCount() {
        return this.masterConfigs.size();
    }

    public String getNoConnectMsg() {
        return "Nėra ryšio";
    }
}
