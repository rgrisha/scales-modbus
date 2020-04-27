package model;

public class DisplayErrorMessage extends DisplayMessage {
    private String errorMessage;

    public DisplayErrorMessage(MasterInfo masterInfo, String errorMessage) {
        super(masterInfo);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return this.errorMessage;
    }

    @Override
    public String toStringForTitle() {
        return "----";
    }
}
