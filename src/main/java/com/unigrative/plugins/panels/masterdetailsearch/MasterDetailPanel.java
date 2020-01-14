/*
 * Created by JFormDesigner on Tue Jan 14 13:11:34 CST 2020
 */

package com.unigrative.plugins.panels.masterdetailsearch;

import com.evnt.client.common.PnlPagedSearch;
import com.evnt.common.MethodConst;
import com.fbi.gui.misc.IconTitleBorderPanel;
import com.fbi.gui.panel.FBSplitPane;
import com.fbi.gui.util.UtilGui;
import com.unigrative.plugins.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * @author Brian Nordstrom
 */
public class MasterDetailPanel extends JPanel {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class)MasterDetailPanel.class);


    private Plugin _plugin;

    public MasterDetailPanel(Plugin plugin) {
        _plugin = plugin; //FOR EVE ACCESS
        initComponents();

        LOGGER.debug("Master Detail Screen Components init");
        this.searchPanel.init();
        this.searchPanel.getPnlPagedSearch().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2){ //double click event
                    MasterDetailPanel.this.loadGenericItemDetails();
                }
            }
        });
        this.searchPanel.getPnlPagedSearch().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == 10){ //enter key
                    MasterDetailPanel.this.loadGenericItemDetails();
                }
            }
        });


    }



    private void viewButtonActionPerformed(ActionEvent e) {
        loadGenericItemDetails();
    }


    private void loadGenericItemDetails(){
        if (this.searchPanel.getPnlPagedSearch().getSelectedRowCount() == 0) {
            UtilGui.showMessageDialog("Select an item to be edited.", "No Item Selected", 0);
            return;
        }
        final int selectedID = this.searchPanel.getPnlPagedSearch().getSelectedID();
        UtilGui.showMessageDialog("Selected item GetID method returned: " + selectedID, "No Item Selected", 0);
    }



    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Brian Nordstrom
        this.splitPanels = new FBSplitPane();
        this.panel1 = new JPanel();
        this.iconTitleBorderPanel1 = new IconTitleBorderPanel();
        this.bottomButtonsPanel = new JPanel();
        this.viewButton = new JButton();
        this.searchPanel = new SearchPanel();

        //======== splitPanels ========
        {
            this.splitPanels.setOneTouchExpandable(true);
            this.splitPanels.setDividerLocation(250);

            //======== panel1 ========
            {
                this.panel1.setMinimumSize(new Dimension(50, 0));
                this.panel1.setPreferredSize(new Dimension(50, 0));
                this.panel1.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax.
                swing. border. EmptyBorder( 0, 0, 0, 0) , "JF\u006frmDes\u0069gner \u0045valua\u0074ion", javax. swing. border
                . TitledBorder. CENTER, javax. swing. border. TitledBorder. BOTTOM, new java .awt .Font ("D\u0069alog"
                ,java .awt .Font .BOLD ,12 ), java. awt. Color. red) ,this.panel1. getBorder
                ( )) ); this.panel1. addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java
                .beans .PropertyChangeEvent e) {if ("\u0062order" .equals (e .getPropertyName () )) throw new RuntimeException
                ( ); }} );
                this.panel1.setLayout(new BorderLayout(5, 5));

                //======== iconTitleBorderPanel1 ========
                {
                    this.iconTitleBorderPanel1.setType(IconTitleBorderPanel.IconConst.Search);
                    this.iconTitleBorderPanel1.setTitle("Search");
                    this.iconTitleBorderPanel1.setLayout(new GridBagLayout());
                    ((GridBagLayout)this.iconTitleBorderPanel1.getLayout()).columnWidths = new int[] {0, 0};
                    ((GridBagLayout)this.iconTitleBorderPanel1.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
                    ((GridBagLayout)this.iconTitleBorderPanel1.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
                    ((GridBagLayout)this.iconTitleBorderPanel1.getLayout()).rowWeights = new double[] {1.0, 0.0, 0.0, 1.0E-4};

                    //======== bottomButtonsPanel ========
                    {
                        this.bottomButtonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

                        //---- viewButton ----
                        this.viewButton.setText("View");
                        this.viewButton.addActionListener(e -> viewButtonActionPerformed(e));
                        this.bottomButtonsPanel.add(this.viewButton);
                    }
                    this.iconTitleBorderPanel1.add(this.bottomButtonsPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 2, 0), 0, 0));
                    this.iconTitleBorderPanel1.add(this.searchPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 2, 0), 0, 0));
                }
                this.panel1.add(this.iconTitleBorderPanel1, BorderLayout.CENTER);
            }
            this.splitPanels.setLeftComponent(this.panel1);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Brian Nordstrom
    private FBSplitPane splitPanels;
    private JPanel panel1;
    private IconTitleBorderPanel iconTitleBorderPanel1;
    private JPanel bottomButtonsPanel;
    private JButton viewButton;
    public SearchPanel searchPanel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables











    public class SearchPanel extends JPanel {


        private SearchPanel() {
            initComponents();
        }

        public void init(){
            this.pnlPagedSearch.init(GenericRowData.getSettings(), _plugin.getEveManager(), MethodConst.GET_PLUGIN_INFO, _plugin.getPluginName(), "List");
        }



        private void searchButtonActionPerformed(ActionEvent e) {
            this.executeSearch();
        }

        public void executeSearch() {
            this.pnlPagedSearch.executeSearch(); //executes the search on the FB paged panel
        }

        private void createUIComponents() {

            //create paged search panel
            this.pnlPagedSearch = new PnlPagedSearch<GenericRowData, GenericDataObject>(){
                protected ArrayList<GenericRowData> getRowData(final ArrayList<GenericDataObject> dataList){
                    final ArrayList<GenericRowData> list = new ArrayList<GenericRowData>();
                    for (final GenericDataObject dataObject : dataList){
                        final GenericRowData row = new GenericRowData(dataObject);
                        list.add(row);
                    }

                    return list;
                }

            };

        }

        public PnlPagedSearch<GenericRowData, GenericDataObject> getPnlPagedSearch() {
            return this.pnlPagedSearch;
        }

        private void initComponents() {
            // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
            // Generated using JFormDesigner Evaluation license - Brian Nordstrom
            createUIComponents();

            this.pnlCriteria = new JPanel();
            this.lblField1 = new JLabel();
            this.textField2 = new JTextField();
            this.lblField2 = new JLabel();
            this.textField3 = new JTextField();
            this.searchButton = new JButton();

            //======== this ========
            setLayout(new BorderLayout(10, 10));

            //======== pnlCriteria ========
            {
                this.pnlCriteria.setLayout(new GridBagLayout());
                ((GridBagLayout)this.pnlCriteria.getLayout()).columnWidths = new int[] {0, 0, 0};
                ((GridBagLayout)this.pnlCriteria.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
                ((GridBagLayout)this.pnlCriteria.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
                ((GridBagLayout)this.pnlCriteria.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

                //---- lblField1 ----
                this.lblField1.setText("ID:");
                this.pnlCriteria.add(this.lblField1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 5, 8, 10), 0, 0));
                this.pnlCriteria.add(this.textField2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 8, 5), 0, 0));

                //---- lblField2 ----
                this.lblField2.setText("Name:");
                this.pnlCriteria.add(this.lblField2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 5, 8, 10), 0, 0));
                this.pnlCriteria.add(this.textField3, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 8, 5), 0, 0));

                //---- searchButton ----
                this.searchButton.setText("Search");
                this.searchButton.setMnemonic('R');
                this.searchButton.addActionListener(e -> searchButtonActionPerformed(e));
                this.pnlCriteria.add(this.searchButton, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 0, 5), 0, 0));
            }
            add(this.pnlCriteria, BorderLayout.NORTH);
            add(this.pnlPagedSearch, BorderLayout.CENTER);
            // JFormDesigner - End of component initialization  //GEN-END:initComponents
        }

        // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
        // Generated using JFormDesigner Evaluation license - Brian Nordstrom
        private JPanel pnlCriteria;
        private JLabel lblField1;
        private JTextField textField2;
        private JLabel lblField2;
        private JTextField textField3;
        private JButton searchButton;
        private PnlPagedSearch<GenericRowData, GenericDataObject> pnlPagedSearch;
        // JFormDesigner - End of variables declaration  //GEN-END:variables
    }
}
