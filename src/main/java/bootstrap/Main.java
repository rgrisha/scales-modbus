package bootstrap;

import gui.UserInterface;
import gui.UserInterfaceSwing;
import model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.function.Consumer;

public class Main {
    private static final Logger logger = LogManager.getLogger(Model.class);

    public static void main(String[] args) {

        Configuration configuration = null;

        try {
            configuration = ConfigurationReader.getConfiguration("config.properties");
        } catch (ConfigurationReaderException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            logger.error("Config reading error: " + e.toString());
            return;
        }

        UserInterface ui = UserInterfaceSwing.newInterface(configuration);
        Consumer<DisplayMessage> weightConsumer = ui.getWeightConsumer();
        Model model = new Model(configuration, weightConsumer);

        for(int i = 0; i < configuration.getMasterCount(); i++) {
            ui.addMaster(configuration.getMasterConfiguration(i));
            model.addMaster(configuration.getMasterConfiguration(i));
        }

        ui.setEventListenerCommandZero(model.getZeroCommandConsumer());

    }
}
