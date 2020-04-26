package model;

public class WeightInfo {
    MasterInfo master;
    Float weight;
    String errorMessage;

    public WeightInfo(MasterInfo master, Float weight, String errorMessage) {
        this.master = master;
        this.weight = weight;
        this.errorMessage = errorMessage;
    }

    public MasterInfo getMaster() {
        return master;
    }

    public Float getWeight() {
        return weight;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
