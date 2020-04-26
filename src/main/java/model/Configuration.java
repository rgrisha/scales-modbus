package model;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

    private int pollPeriodMs;
    private int connectRetryTimeoutSec;
    private List<MasterInfo> masterConfigs = new ArrayList<>();
    private String setZeroText;
    private String connectingMessage;

    public int getPollPeriodMs() {
        return pollPeriodMs;
    }

    public void setPollPeriodMs(int pollPeriodMs) {
        this.pollPeriodMs = pollPeriodMs;
    }

    public int getConnectRetryTimeoutSec() {
        return connectRetryTimeoutSec;
    }

    public void setConnectRetryTimeoutSec(int connectRetryTimeoutSec) {
        this.connectRetryTimeoutSec = connectRetryTimeoutSec;
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
