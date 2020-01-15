package com.unigrative.plugins.panels.masterdetailsearch;


import com.fbi.gui.rowdata.RowDataToolTip;
import com.fbi.gui.table.FBTableColumn;
import com.fbi.gui.table.FBTableColumnEditable;
import com.fbi.gui.table.FBTableColumnSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericRow implements RowDataToolTip { //TOOLTIP IS FANCIER THAN JUST ROWDATA

    private static final Logger LOGGER = LoggerFactory.getLogger((Class) GenericRow.class);
    private static int colCount;

    //LIST OF COLUMNS
    private static FBTableColumn colDataObjectId;
    private static FBTableColumn colDataObjectName;
    //public static FBTableColumn colTimeFinished;
    //public static FBTableColumn colTimeStarted;
    //public static FBTableColumn colTimeUploaded;

    private GenericDataObject dataObject; //this is the object that is tied to the row. TABLE COLUMN TYPES NEED TO MATCH THE DATA TYPE OF THE CLASS PROPERTIES

    public GenericRow(GenericDataObject dataObject) {
        this.dataObject = dataObject;
    }

    private static void init(){

        //initialize columns
        colCount = 0;
        colDataObjectId = new FBTableColumn(colCount++, "Id", String.class, 40,"", FBTableColumnEditable.NOT_EDITABLE,true,false);
        colDataObjectName = new FBTableColumn(colCount++, "Name", String.class, 100, "", FBTableColumnEditable.ON_CLICK, false, true);

        //OTHER COLUMN EXAMPLES
//        colStatus = new FBTableColumn(colCount++, "Status", ImageIcon.class, 40,"", FBTableColumnEditable.NOT_EDITABLE,false,false); //LOOK AT PickItemStatusConst CLASS TO USE ICONS
//        colNumber = new FBTableColumn(colCount++, "Number", String.class, 75,"",FBTableColumnEditable.NOT_EDITABLE,false,true);
//        colDescription = new FBTableColumn(colCount++, "Description", String.class, 150);
//        colQty = new FBTableColumn(colCount++, "Qty", Quantity.class, 75, "", FBTableColumnEditable.ON_CLICK, false, false);
//        colUOM = new FBTableColumn(colCount++, "UOM",  String.class, 50);
//        colLocation = new FBTableColumn(colCount++,"Location",  String.class, 150);
//        colType = new FBTableColumn(colCount++, "Type",  ImageIcon.class, 0,"",FBTableColumnEditable.NOT_EDITABLE,true,false);
//        colOrder = new FBTableColumn(colCount++, "Order",String.class, 0, "", FBTableColumnEditable.NOT_EDITABLE, true, false);
//        colDestLocation = new FBTableColumn(colCount++, "DestLocation", String.class, 0, "", FBTableColumnEditable.NOT_EDITABLE, true, false);
//        colTracking = new FBTableColumn(colCount++, "Tracking",  Tracking.class, 0, "", FBTableColumnEditable.NOT_EDITABLE, true, false);
//        colAltNumber = new FBTableColumn(colCount++, "AltNumber", String.class, 0, "", FBTableColumnEditable.NOT_EDITABLE, true, false);
//        colPickLocation = new FBTableColumn(colCount++, "Pick Location", PointOfSaleLocation.class, 100, "", FBTableColumnEditable.ON_CLICK, false, false);



    }

    @Override
    public int getID() {
        return dataObject.getId(); //CHANGE THIS IF A DIFFERENT FIELD NEEDS TO BE RETURNED. THIS FIELD IS WHAT IS USED TO LOAD DETAILS WITH
    }

    @Override
    public String getValue() {
        return dataObject.getId() + " other information or fields to return";
    }

    @Override
    public Object[] getRow() {
        final Object[] values = new Object[GenericRow.colCount]; //array to hold the column values

        //ITERATE THE DATA OBJECT PROPERTIES AND ASSIGN THEM TO THE MATCHING COLUMN INDEX IN THE ARRAY
        values[GenericRow.colDataObjectId.getColIndex()] = dataObject.getId();
        values[GenericRow.colDataObjectName.getColIndex()] = dataObject.getName();

        return values;
    }

    @Override
    public void setValueAt(int columnId, Object value) { //METHOD TO SET THE VALUE AT COLUMN ID SPECIFIED WITH OBJECT PROVIDED
        //IF READ ONLY, DON'T NEED
        //SEARCH WOULD BE READ ONLY

        //OTHERWISE for Regular tables
        if (value instanceof String && columnId == colDataObjectName.getColIndex()){
            dataObject.setName(value.toString());
        }
    }

    @Override
    public boolean isCellEditable(int i) {
        return false;
    }

    public static FBTableColumnSettings getSettings(){
        final FBTableColumnSettings settings = new FBTableColumnSettings(true, true);

        //ADD ALL COLUMNS
        settings.addColumn(GenericRow.colDataObjectId);
        settings.addColumn(GenericRow.colDataObjectName);

        return settings;
    }

    public String getToolTip(final int columnIndex) {
        //set tool tip values based on the column index

        if (columnIndex == GenericRow.colDataObjectName.getColIndex()) {
            return this.dataObject.getName();
        }
//        if (columnIndex == BIPickItemRowData.colType.getColIndex()) {
//            switch (this.item.getPickItemType()) {
//                case NORMAL: {
//                    return "This item has NO restrictions on available pick locations.";
//                }
//                case PFL: {
//                    return "This item must be picked from certain locations.";
//                }
//                case BTO: {
//                    return "Built to Order: This item will auto commit when the work order is fulfilled.";
//                }
//            }
//        }
//        else if (columnIndex == BIPickItemRowData.colAltNumber.getColIndex()) {
//            return "Display customer product number or vendor part number.";
//        }
        return null;
    }

    static{
        init();
    }
}
