/*
 * Created by JFormDesigner on Sun Jan 12 12:40:24 CST 2020
 */

package com.unigrative.plugins.apiExtension.panels;

import com.fbi.gui.misc.*;
import com.fbi.gui.util.UtilGui;
import com.printnode.api.impl.APIClient;
import com.printnode.api.impl.Auth;
import com.printnode.api.impl.Printer;
import com.unigrative.plugins.apiExtension.ApiExtensionsPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author unknown
 */
public class SettingsPanel extends JPanel {

    private static final Logger LOGGER = LoggerFactory.getLogger((Class)SettingsPanel.class);
    private ApiExtensionsPlugin apiExtensionsPlugin;

    public SettingsPanel(final ApiExtensionsPlugin genericPlugin) {
        this.apiExtensionsPlugin = genericPlugin; //NEEDED FOR ACCESS TO DB PROPERTIES IF DESIRED
        initComponents();
    }

    public static void main(String[] args) {
        SettingsPanel panel = new SettingsPanel(null);

        JFrame frame = new JFrame("Test");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void button1ActionPerformed(ActionEvent e) {
        //test print node api connection
        String apiKey = txtAPIkey.getText();

        if (apiKey.isEmpty()){
            UtilGui.showMessageDialog("Api Key cannot be blank");
            return;
        }

        Auth auth = new Auth();
        auth.setApiKey(apiKey);

        APIClient client = new APIClient(auth);
        try {

        txtOutput.append(client.getWhoami().getEmail());
            txtOutput.append(System.lineSeparator());
            txtOutput.append(client.getWhoami().getFirstname());
            txtOutput.append(" ");
            txtOutput.append(client.getWhoami().getLastname());
            txtOutput.append(System.lineSeparator());
            txtOutput.append(System.lineSeparator());
            txtOutput.append(System.lineSeparator());

            Printer[] printers = client.getPrinters("");

            for (Printer printer: printers
                 ) {
                txtOutput.append(printer.getName());
                txtOutput.append(System.lineSeparator());
                txtOutput.append(printer.getComputer().getName());
                txtOutput.append(System.lineSeparator());
                txtOutput.append(System.lineSeparator());
            }
        }
        catch (IOException ex){
            txtOutput.append("Error:");
            txtOutput.append(System.lineSeparator());
            txtOutput.append(ex.getMessage());
        }
    }


    public void loadSettings() {
        LOGGER.debug("Loading Settings");
        String key = this.apiExtensionsPlugin.getProperty("PrintNodeApiKey");
        this.txtAPIkey.setText(key);
        LOGGER.debug("Settings Loaded");

    }
    public void saveSettings(){
        LOGGER.debug("Saving settings");

        final Map<String, String> properties = new HashMap<>();

        //add in settings
        properties.put("PrintNodeApiKey", txtAPIkey.getText());

        this.apiExtensionsPlugin.savePluginProperties(properties);

        LOGGER.debug("Settings Saved");
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        iconTitleBorderPanel1 = new IconTitleBorderPanel();
        label1 = new JLabel();
        txtAPIkey = new JTextField();
        btnTest = new JButton();
        scrollPane1 = new JScrollPane();
        txtOutput = new JTextArea();

        //======== this ========
        setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setLayout(new GridBagLayout());
            ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {0, 0};
            ((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {0, 0};
            ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
            ((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

            //======== iconTitleBorderPanel1 ========
            {
                iconTitleBorderPanel1.setIcon(new ImageIcon(getClass().getResource("/icon16/arrowsandnavigation/navigation2/navigate_right2.png")));
                iconTitleBorderPanel1.setTitle("PrintNode Settings");
                iconTitleBorderPanel1.setLayout(new GridBagLayout());
                ((GridBagLayout)iconTitleBorderPanel1.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
                ((GridBagLayout)iconTitleBorderPanel1.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0};
                ((GridBagLayout)iconTitleBorderPanel1.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 1.0E-4};
                ((GridBagLayout)iconTitleBorderPanel1.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

                //---- label1 ----
                label1.setText("PrintNode API Key");
                iconTitleBorderPanel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));
                iconTitleBorderPanel1.add(txtAPIkey, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- btnTest ----
                btnTest.setText("Test");
                btnTest.addActionListener(e -> button1ActionPerformed(e));
                iconTitleBorderPanel1.add(btnTest, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //======== scrollPane1 ========
                {
                    scrollPane1.setPreferredSize(new Dimension(497, 150));

                    //---- txtOutput ----
                    txtOutput.setLineWrap(true);
                    scrollPane1.setViewportView(txtOutput);
                }
                iconTitleBorderPanel1.add(scrollPane1, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));
            }
            panel1.add(iconTitleBorderPanel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(3, 3, 0, 0), 0, 0));
        }
        add(panel1, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private IconTitleBorderPanel iconTitleBorderPanel1;
    private JLabel label1;
    private JTextField txtAPIkey;
    private JButton btnTest;
    private JScrollPane scrollPane1;
    private JTextArea txtOutput;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
