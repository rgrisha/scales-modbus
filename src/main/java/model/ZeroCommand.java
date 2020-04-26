package model;

public class ZeroCommand {
    MasterInfo masterInfo;
    int result;

    public ZeroCommand(MasterInfo masterInfo, int result) {
        this.masterInfo = masterInfo;
        this.result = result;
    }

    public MasterInfo getMasterInfo() {
        return masterInfo;
    }

    public int getResult() {
        return result;
    }
}
