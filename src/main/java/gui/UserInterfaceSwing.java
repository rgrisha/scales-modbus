package gui;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class UserInterfaceSwing implements UserInterface, ActionListener {

    static String ZERO_COMMAND = "ZERO";

    JFrame frame = new JFrame("");
    JPanel pane = new JPanel(new BorderLayout());

    Consumer<ZeroCommand> sendZeroCommand = null;
    private Configuration configuration;

    Map<String, JLabel> masterInfoLabel = new HashMap<>();
    Map<String, String> titleDisplay = new HashMap<>();
    Map<String, MasterInfo> zeroCommands = new HashMap<>();

    private UserInterfaceSwing() {}

    public static UserInterface newInterface(Configuration configuration) {
        UserInterfaceSwing ui = new UserInterfaceSwing();
        ui.setConfiguration(configuration);
        ui.init();
        return ui;
    }

    private void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    private void init() {

        frame.setContentPane(pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 120);
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
   }

    @Override
    public void addMaster(MasterInfo mi) {

        Box masterBox = Box.createHorizontalBox();

        Font font = new Font("Arial", Font.PLAIN, 20);
        JLabel labelName = new JLabel(mi.getName());
        labelName.setPreferredSize(new Dimension(150, 50));
        labelName.setFont(font);
        labelName.setHorizontalAlignment(SwingConstants.LEFT);
        masterBox.add(Box.createHorizontalStrut(10));
        masterBox.add(labelName);
        masterBox.add(Box.createHorizontalGlue());

        JLabel labelWeight = new JLabel();
        labelWeight.setFont(font);
        masterInfoLabel.put(mi.getName(), labelWeight);
        masterBox.add(labelWeight);

        JButton zeroButton = new JButton(configuration.getSetZeroText());
        String zeroCommand = ZERO_COMMAND + ";" + mi.getName();
        zeroButton.setActionCommand(zeroCommand);
        zeroCommands.put(zeroCommand, mi);
        zeroButton.addActionListener(this);
        zeroButton.setPreferredSize(new Dimension(80, 30));
        masterBox.add(Box.createHorizontalGlue());
        masterBox.add(zeroButton);
        masterBox.add(Box.createHorizontalStrut(10));

        pane.add(Box.createRigidArea(new Dimension(0,10)));
        pane.add(masterBox);

        titleDisplay.put(mi.getName(), "");

        frame.setVisible(true);
    }

    private void displayInfo(DisplayMessage displayMessage) {
        String masterName = displayMessage.getMasterInfo().getName();
        JLabel weightLabel = masterInfoLabel.get(masterName);
        weightLabel.setText(displayMessage.toString());
        titleDisplay.put(masterName, displayMessage.toStringForTitle());
        refreshTitle();
    }

    private void refreshTitle() {
        String acc = "";
        String delim = "";
        for(String titleVal: titleDisplay.values()) {
            acc = acc + delim + titleVal;
            delim = " | ";
        }
        frame.setTitle(acc);
    }

    @Override
    public Consumer<DisplayMessage> getWeightConsumer() {
        return this::displayInfo;
    }

    @Override
    public void setEventListenerCommandZero(Consumer<ZeroCommand> zc) {
        sendZeroCommand = zc;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String actionCommand = actionEvent.getActionCommand();

        MasterInfo masterInfo;

        masterInfo = zeroCommands.get(actionEvent.getActionCommand());
        if(masterInfo != null) {
            sendZeroCommand.accept(new ZeroCommand(masterInfo, 0));
        }
    }
}
