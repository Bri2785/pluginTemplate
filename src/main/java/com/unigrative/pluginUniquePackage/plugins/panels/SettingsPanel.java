/*
 * Created by JFormDesigner on Sun Jan 12 12:40:24 CST 2020
 */

package com.unigrative.pluginUniquePackage.plugins.panels;

import java.awt.event.*;

import com.fbi.gui.util.UtilGui;
import com.unigrative.pluginUniquePackage.plugins.GenericPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

/**
 * @author unknown
 */
public class SettingsPanel extends JPanel {

    private static final Logger LOGGER = LoggerFactory.getLogger((Class)SettingsPanel.class);
    private GenericPlugin genericPlugin;

    public SettingsPanel(final GenericPlugin genericPlugin) {
        this.genericPlugin = genericPlugin; //NEEDED FOR ACCESS TO DB PROPERTIES IF DESIRED
        initComponents();
    }

    private void button1ActionPerformed(ActionEvent e) {

        UtilGui.showMessageDialog("Test Message");

    }

    public void loadSettings(){
        LOGGER.debug("Loading Settings");
        //this.txtApiToken.setText(this.restApiPlugin.getPluginData(KeyConstants.API_SECURITY_TOKEN));

        LOGGER.debug("Settings Loaded");
    }

    public void saveSettings(){
        LOGGER.debug("Saving settings");

        final Map<String, String> staticData = new HashMap<>();
        //staticData.put(KeyConstants.API_SECURITY_TOKEN, this.txtApiToken.getText());

        this.genericPlugin.savePluginData(staticData);

        LOGGER.debug("Settings Saved");
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        textField1 = new JTextField();
        scrollPane1 = new JScrollPane();
        txtOutput = new JTextArea();
        panel2 = new JPanel();
        textArea1 = new JTextArea();
        btnTest = new JButton();
        btnCancel = new JButton();

        //======== this ========
        setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setLayout(new GridBagLayout());
            ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {0, 0};
            ((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0};
            ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
            ((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0, 0.0, 1.0E-4};
            panel1.add(textField1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));

            //======== scrollPane1 ========
            {
                scrollPane1.setViewportView(txtOutput);
            }
            panel1.add(scrollPane1, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));

            //======== panel2 ========
            {
                panel2.setLayout(new GridBagLayout());
                ((GridBagLayout)panel2.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
                ((GridBagLayout)panel2.getLayout()).rowHeights = new int[] {0, 0};
                ((GridBagLayout)panel2.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0, 1.0E-4};
                ((GridBagLayout)panel2.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};
                panel2.add(textArea1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- btnTest ----
                btnTest.setText("Test");
                btnTest.addActionListener(e -> button1ActionPerformed(e));
                panel2.add(btnTest, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- btnCancel ----
                btnCancel.setText("Cancel");
                panel2.add(btnCancel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            panel1.add(panel2, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 10, 10), 0, 0));
        }
        add(panel1, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JTextField textField1;
    private JScrollPane scrollPane1;
    private JTextArea txtOutput;
    private JPanel panel2;
    private JTextArea textArea1;
    private JButton btnTest;
    private JButton btnCancel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
