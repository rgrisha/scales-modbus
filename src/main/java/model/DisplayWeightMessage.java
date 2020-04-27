package model;

public class DisplayWeightMessage extends DisplayMessage {

    float weight;

    public DisplayWeightMessage(MasterInfo masterInfo, float weight) {
        super(masterInfo);
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return Integer.toString(Math.round(weight));
    }

    @Override
    public String toStringForTitle() {
        return this.toString();
    }
}
