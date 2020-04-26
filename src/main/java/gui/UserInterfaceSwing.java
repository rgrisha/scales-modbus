package gui;

import model.Configuration;
import model.MasterInfo;
import model.WeightInfo;
import model.ZeroCommand;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
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
        frame.setSize(300, 200);
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
   }

    @Override
    public void addMaster(MasterInfo mi) {

        JPanel masterPane = new JPanel();
        masterPane.setLayout(new BoxLayout(masterPane, BoxLayout.LINE_AXIS));

        Font font = new Font("Arial", Font.PLAIN, 20);
        JLabel labelName = new JLabel(mi.getName());
        labelName.setFont(font);
        labelName.setAlignmentX(Component.LEFT_ALIGNMENT);
        masterPane.add(labelName);
        masterPane.add(Box.createRigidArea(new Dimension(10,0)));

        JLabel labelWeight = new JLabel();
        labelWeight.setFont(font);
        masterInfoLabel.put(mi.getName(), labelWeight);
        masterPane.add(labelWeight);
        masterPane.add(Box.createRigidArea(new Dimension(10,0)));

        JButton zeroButton = new JButton(configuration.getSetZeroText());
        String zeroCommand = ZERO_COMMAND + ";" + mi.getName();
        zeroButton.setActionCommand(zeroCommand);
        zeroCommands.put(zeroCommand, mi);
        zeroButton.addActionListener(this);
        zeroButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        masterPane.add(zeroButton);

        pane.add(Box.createRigidArea(new Dimension(0,10)));
        pane.add(masterPane);

        frame.setVisible(true);
    }

    private void displayWeight(WeightInfo weightInfo) {
        JLabel weightLabel = masterInfoLabel.get(weightInfo.getMaster().getName());
        if(weightLabel == null) {
            return;
        }
        String weightStr = weightInfo.getErrorMessage();
        if(weightInfo.getWeight() != null) {
            weightStr = Integer.toString(Math.round(weightInfo.getWeight()));
        }
        weightLabel.setText(weightStr);
        frame.setTitle(weightStr);
    }

    @Override
    public Consumer<WeightInfo> getWeightConsumer() {
        return this::displayWeight;
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
