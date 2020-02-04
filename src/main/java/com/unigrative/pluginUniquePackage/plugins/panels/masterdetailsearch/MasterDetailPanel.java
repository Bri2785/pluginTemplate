/*
 * Created by JFormDesigner on Tue Jan 14 13:11:34 CST 2020
 */

package com.unigrative.pluginUniquePackage.plugins.panels.masterdetailsearch;

import com.evnt.client.common.EVEManagerUtil;
import com.fbi.gui.misc.IconTitleBorderPanel;
import com.fbi.gui.panel.FBSplitPane;
import com.fbi.gui.util.UtilGui;
import com.unigrative.pluginUniquePackage.plugins.GenericPlugin;
import com.unigrative.pluginUniquePackage.plugins.panels.masterdetailsearch.details.table.*;
import com.unigrative.pluginUniquePackage.plugins.util.sql.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Brian Nordstrom
 */
public class MasterDetailPanel extends FBSplitPane {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class)MasterDetailPanel.class);

    private SqlUtil sqlUtil;

    private GenericPlugin _Generic_plugin;

    public MasterDetailPanel(GenericPlugin genericPlugin) {
        try {
            _Generic_plugin = genericPlugin; //FOR EVE ACCESS
            initComponents();

            LOGGER.debug("Master Detail Screen Components init");
            this.searchPanel.init();
            this.searchPanel.getSearchPanelFromSql().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2){ //double click event
                        MasterDetailPanel.this.loadGenericItemDetails();
                    }
                }
            });
            this.searchPanel.getSearchPanelFromSql().addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() == 10){ //enter key
                        MasterDetailPanel.this.loadGenericItemDetails();
                    }
                }
            });


        }
        catch (Exception e){
            LOGGER.error("Error Initializing", e);
        }
    }



    private void viewButtonActionPerformed(ActionEvent e) {
        loadGenericItemDetails();
    }


    private void loadGenericItemDetails(){
        //TODO: THIS CHANGES BASED ON IF WE ARE LOADING A SINGLE RECORD OR A ONE-TO-MANY DETAILS SETUP

        //SINGLE



        //LIST DETAILS
        //get table with actions object and load data on that side
        if (this.searchPanel.getSearchPanelFromSql().getSelectedRowCount() > 1){
            UtilGui.showMessageDialog("Can't load more than one record");
        }
        else if (this.searchPanel.getSearchPanelFromSql().getSelectedRowCount() == 1) {
            final int selectedId = this.searchPanel.searchPanelFromSql.getSelectedID();
            this.tablePanelWithActions1.loadData(selectedId);
        }



//        if (this.searchPanel.getPnlPagedSearch().getSelectedRowCount() == 0) {
//            UtilGui.showMessageDialog("Select an item to be edited.", "No Item Selected", 0);
//            return;
//        }
//        final int selectedID = this.searchPanel.getPnlPagedSearch().getSelectedID();
//        UtilGui.showMessageDialog("Selected item GetID method returned: " + selectedID, "No Item Selected", 0);
    }

    public TablePanelWithActions getTablePanelWithActions1() {
        return this.tablePanelWithActions1;
    }



    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Brian Nordstrom
        this.panel1 = new JPanel();
        this.iconTitleBorderPanel1 = new IconTitleBorderPanel();
        this.bottomButtonsPanel = new JPanel();
        this.viewButton = new JButton();
        this.searchPanel = new SearchPanel();
        this.panel2 = new JPanel();
        this.tablePanelWithActions1 = new TablePanelWithActions();

        //======== this ========

        //======== panel1 ========
        {
            this.panel1.setMinimumSize(new Dimension(50, 0));
            this.panel1.setPreferredSize(new Dimension(50, 0));
            this.panel1.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border
            . EmptyBorder( 0, 0, 0, 0) , "JF\u006frm\u0044es\u0069gn\u0065r \u0045va\u006cua\u0074io\u006e", javax. swing. border. TitledBorder. CENTER, javax
            . swing. border. TitledBorder. BOTTOM, new java .awt .Font ("D\u0069al\u006fg" ,java .awt .Font .BOLD ,
            12 ), java. awt. Color. red) ,this.panel1. getBorder( )) ); this.panel1. addPropertyChangeListener (new java. beans
            . PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062or\u0064er" .equals (e .
            getPropertyName () )) throw new RuntimeException( ); }} );
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
        setLeftComponent(this.panel1);

        //======== panel2 ========
        {
            this.panel2.setLayout(new BorderLayout());
            this.panel2.add(this.tablePanelWithActions1, BorderLayout.CENTER);
        }
        setRightComponent(this.panel2);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Brian Nordstrom
    private JPanel panel1;
    private IconTitleBorderPanel iconTitleBorderPanel1;
    private JPanel bottomButtonsPanel;
    private JButton viewButton;
    public SearchPanel searchPanel;
    private JPanel panel2;
    private TablePanelWithActions tablePanelWithActions1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables











    public class SearchPanel extends JPanel {




        private SearchPanel() {
            initComponents();
            //THE STRING NAMES ARE THE FIELD IN THE WHERE CLAUSE
            //THE MAP THESE ARE ADDED TO WILL BE PASSED TO THE MAP->PARAM_STRING METHOD TO ADD THEM INTO THE QUERY
            this.searchPanelFromSql.addFilter("product.id", (Component) this.txtID);
            this.searchPanelFromSql.addFilter("product.num", (Component) this.txtName);

            sqlUtil = new SqlUtil();
        }

        public void init(){
            //CHANGE QUERY FILE NAME IF NEEDED
            this.searchPanelFromSql.init(GenericRow.getSettings(), EVEManagerUtil.getEveManager(), "/searchSqlQueries/query.sql", sqlUtil);
        }



        private void searchButtonActionPerformed(ActionEvent e) {
            this.executeSearch();
        }

        public void executeSearch() {
            this.searchPanelFromSql.executeSearch(); //executes the search on the FB paged panel
        }

        private void createUIComponents() {

//            create paged search panel
            this.searchPanelFromSql = new SearchPanelFromSql<GenericRow, GenericDataObject>(){
                @Override
                protected ArrayList<GenericRow> getRowData(List<GenericDataObject> genericDataObjects) {
                        final ArrayList<GenericRow> list = new ArrayList<GenericRow>();
                        for (final GenericDataObject dataObject : genericDataObjects){
                            final GenericRow row = new GenericRow(dataObject);
                            list.add(row);
                        }

                        return list;

                }
            };

        }

        public SearchPanelFromSql<GenericRow, GenericDataObject> getSearchPanelFromSql() {
            return this.searchPanelFromSql;
        }

        private void txtIDKeyPressed(KeyEvent e) {
            if (e.getKeyCode() == 10) {
                executeSearch();
            }
        }

        private void txtNameKeyPressed(KeyEvent e) {
            if (e.getKeyCode() == 10) {
                executeSearch();
            }
        }

        private void searchButtonKeyPressed(KeyEvent e) {
            if (e.getKeyCode() == 10) {
                executeSearch();
            }
        }

        private void initComponents() {
            // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
            // Generated using JFormDesigner Evaluation license - Brian Nordstrom
            createUIComponents();

            this.pnlCriteria = new JPanel();
            this.lblField1 = new JLabel();
            this.txtID = new JTextField();
            this.lblField2 = new JLabel();
            this.txtName = new JTextField();
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

                //---- txtID ----
                this.txtID.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        txtIDKeyPressed(e);
                    }
                });
                this.pnlCriteria.add(this.txtID, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 8, 5), 0, 0));

                //---- lblField2 ----
                this.lblField2.setText("Name:");
                this.pnlCriteria.add(this.lblField2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 5, 8, 10), 0, 0));

                //---- txtName ----
                this.txtName.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        txtNameKeyPressed(e);
                    }
                });
                this.pnlCriteria.add(this.txtName, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 8, 5), 0, 0));

                //---- searchButton ----
                this.searchButton.setText("Search");
                this.searchButton.setMnemonic('R');
                this.searchButton.addActionListener(e -> searchButtonActionPerformed(e));
                this.searchButton.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        searchButtonKeyPressed(e);
                    }
                });
                this.pnlCriteria.add(this.searchButton, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 0, 5), 0, 0));
            }
            add(this.pnlCriteria, BorderLayout.NORTH);
            add(this.searchPanelFromSql, BorderLayout.CENTER);
            // JFormDesigner - End of component initialization  //GEN-END:initComponents
        }

        // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
        // Generated using JFormDesigner Evaluation license - Brian Nordstrom
        private JPanel pnlCriteria;
        private JLabel lblField1;
        private JTextField txtID;
        private JLabel lblField2;
        private JTextField txtName;
        private JButton searchButton;
        public SearchPanelFromSql<GenericRow, GenericDataObject> searchPanelFromSql;
        // JFormDesigner - End of variables declaration  //GEN-END:variables
    }
}
