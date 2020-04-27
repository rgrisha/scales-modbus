package model;

public abstract class DisplayMessage {
    MasterInfo masterInfo;
    public abstract String toString();
    public abstract String toStringForTitle();

    public DisplayMessage(MasterInfo masterInfo) {
        this.masterInfo = masterInfo;
    }

    public MasterInfo getMasterInfo() {
        return masterInfo;
    }
}
