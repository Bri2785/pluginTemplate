/*
 * Created by JFormDesigner on Tue Jan 21 16:06:16 CST 2020
 */

package com.unigrative.pluginUniquePackage.plugins.panels.masterdetailsearch.details.table;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import com.fbi.gui.button.*;
import com.fbi.gui.misc.GUIProperties;
import com.fbi.gui.table.*;
import com.unigrative.pluginUniquePackage.plugins.GenericPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Brian Nordstrom
 */
public class TablePanelWithActions extends JPanel {

    private static final Logger LOGGER = LoggerFactory.getLogger(TablePanelWithActions.class);
    private FBTableModel<GenericItemRow> tblMdlGenericItems; //model for our table

    private GenericPlugin plugin;


    public TablePanelWithActions() {
        initComponents();

        //should be model
        //GUIProperties.registerComponent(detailsTable, GenericPlugin.MODULE_FRIENDLY_NAME);
    }

    private void initTable(GenericPlugin plugin){
        this.plugin = plugin;
        GUIProperties.registerComponent(this.tblMdlGenericItems, this.plugin.getModuleName()); //register table model so it saves the column layout for the user
        this.populateTable(this.detailsTable, new ArrayList<>(), 0); //load a blank table so we can see the layout
    }

    private void btnAddActionPerformed(ActionEvent e) {
        LOGGER.debug("Adding Seed");

        //SHOW CUSTOM DIALOG BOX WITH THE FIELDS THAT WE NEED TO ADD/EDIT A RECORD
        //ALL OF THE UPDATE REQUESTS TO THE SERVER HAPPEN IN THE DIALOG BOX
        //IF THAT ERRORS, IT DISPLAYS IT AND STAYS ON THE DIALOG BOX
        //SEE CUSTOMFIELDDIALOG -> OKBUTTONACTIONPERFORMED


        LOGGER.debug("Seed added");
    }

    private void btnDeleteActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void btnEditActionPerformed(ActionEvent e) {
        // TODO add your code here
    }


    private FBTableModel<GenericItemRow> populateTable(FBTable tblGenericItems, List<GenericItemDataObject> genericItems, int objectId) {
        //TODO: IF WE ARE USING OUR NEW EVE MODULE ON THE BACK END, WE NEED TO CHANGE THE GENERIC ITEM DATA OBJECT
        //TO OUR OWN MODEL (EXAMPLE MODELS->SEED SINCE IT IMPLEMENTS FBDATA ALSO
        //ALSO NEED TO CHANGE THE GENERIC ITEM ROW AS WELL TO USE OUR OWN MODEL
        try{
            FBTableModel<GenericItemRow> model = new FBTableModel<GenericItemRow>(tblGenericItems, GenericItemRow.getSettings());

            if (genericItems.size() > 0){

                for (GenericItemDataObject item: genericItems) {
                    model.addRow( new GenericItemRow((item)));
                }
                return model;
            }
            else{
                LOGGER.debug("Items list is blank");
            }
        }
        catch (Exception e){
            LOGGER.error("Error populating table", e);
        }
        return null;
    }

    public void loadData(final int objectId){
        //this method loads data for the table from the supplied object id that is sent from the search panel

        GUIProperties.saveComponents(this.plugin.getModuleName()); //save any table layout changes before loading the model

        try{

            //GET ROW DATA FROM OUR OBJECT ID
            //CREATE EVEVENT REQUEST TO WHATEVER MODULE WE NEED TO GET INFO BACK


            //this.tblMdlGenericItems = this.populateTable(this.detailsTable, , objectId);


        }
        catch (Exception e){
            LOGGER.error("Error loading data");
        }

    }




    public FBTable getDetailsTable() {
        return detailsTable;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Brian Nordstrom
        scrollPane1 = new JScrollPane();
        detailsTable = new FBTable();
        fBSideToolbar1 = new FBSideToolbar();
        btnAdd = new FBSideToolbarButton();
        btnEdit = new FBSideToolbarButton();
        btnDelete = new FBSideToolbarButton();

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax.
        swing. border. EmptyBorder( 0, 0, 0, 0) , "JFor\u006dDesi\u0067ner \u0045valu\u0061tion", javax. swing. border
        . TitledBorder. CENTER, javax. swing. border. TitledBorder. BOTTOM, new java .awt .Font ("Dia\u006cog"
        ,java .awt .Font .BOLD ,12 ), java. awt. Color. red) , getBorder
        ( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java
        .beans .PropertyChangeEvent e) {if ("bord\u0065r" .equals (e .getPropertyName () )) throw new RuntimeException
        ( ); }} );
        setLayout(new GridBagLayout());
        ((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0, 0};
        ((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0};
        ((GridBagLayout)getLayout()).columnWeights = new double[] {1.0, 0.0, 1.0E-4};
        ((GridBagLayout)getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

        //======== scrollPane1 ========
        {
            scrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane1.setViewportView(detailsTable);
        }
        add(scrollPane1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 5), 0, 0));

        //======== fBSideToolbar1 ========
        {

            //---- btnAdd ----
            btnAdd.setIcon(new ImageIcon(getClass().getResource("/icon24/textanddocuments/documents/document_new.png")));
            btnAdd.setButtonSize(new Dimension(40, 40));
            btnAdd.addActionListener(e -> btnAddActionPerformed(e));
            fBSideToolbar1.add(btnAdd);

            //---- btnEdit ----
            btnEdit.setIcon(new ImageIcon(getClass().getResource("/icon24/toolbar/edit/edit.png")));
            btnEdit.setButtonSize(new Dimension(40, 40));
            btnEdit.addActionListener(e -> btnEditActionPerformed(e));
            fBSideToolbar1.add(btnEdit);

            //---- btnDelete ----
            btnDelete.setButtonSize(new Dimension(40, 40));
            btnDelete.setIcon(new ImageIcon(getClass().getResource("/icon24/toolbar/others/delete.png")));
            btnDelete.addActionListener(e -> btnDeleteActionPerformed(e));
            fBSideToolbar1.add(btnDelete);
        }
        add(fBSideToolbar1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Brian Nordstrom
    private JScrollPane scrollPane1;
    private FBTable detailsTable;
    private FBSideToolbar fBSideToolbar1;
    private FBSideToolbarButton btnAdd;
    private FBSideToolbarButton btnEdit;
    private FBSideToolbarButton btnDelete;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
