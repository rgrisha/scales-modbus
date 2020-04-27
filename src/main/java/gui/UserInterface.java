package gui;

import model.DisplayMessage;
import model.MasterInfo;
import model.WeightInfo;
import model.ZeroCommand;

import java.util.function.Consumer;

public interface UserInterface {
    Consumer<DisplayMessage> getWeightConsumer();
    void setEventListenerCommandZero(Consumer<ZeroCommand> e);
    void addMaster(MasterInfo mi);
}
