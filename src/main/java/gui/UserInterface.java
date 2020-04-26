package gui;

import model.MasterInfo;
import model.WeightInfo;
import model.ZeroCommand;

import java.util.function.Consumer;

public interface UserInterface {
    Consumer<WeightInfo> getWeightConsumer();
    void setEventListenerCommandZero(Consumer<ZeroCommand> e);
    void addMaster(MasterInfo mi);
}
