/*
 * Created by JFormDesigner on Sun Jan 12 12:40:24 CST 2020
 */

package com.unigrative.plugins.panels;

import java.awt.event.*;

import com.unigrative.plugins.GenericPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
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



    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Brian Nordstrom
        panel1 = new JPanel();
        textField1 = new JTextField();
        scrollPane1 = new JScrollPane();
        txtOutput = new JTextArea();
        panel2 = new JPanel();
        btnTest = new JButton();
        btnCancel = new JButton();

        //======== this ========
        setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder (
        new javax . swing. border .EmptyBorder ( 0, 0 ,0 , 0) ,  "JFor\u006dDesi\u0067ner \u0045valu\u0061tion"
        , javax. swing .border . TitledBorder. CENTER ,javax . swing. border .TitledBorder . BOTTOM
        , new java. awt .Font ( "Dia\u006cog", java .awt . Font. BOLD ,12 )
        ,java . awt. Color .red ) , getBorder () ) );  addPropertyChangeListener(
        new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java . beans. PropertyChangeEvent e
        ) { if( "bord\u0065r" .equals ( e. getPropertyName () ) )throw new RuntimeException( )
        ;} } );
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
    // Generated using JFormDesigner Evaluation license - Brian Nordstrom
    private JPanel panel1;
    private JTextField textField1;
    private JScrollPane scrollPane1;
    private JTextArea txtOutput;
    private JPanel panel2;
    private JButton btnTest;
    private JButton btnCancel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
