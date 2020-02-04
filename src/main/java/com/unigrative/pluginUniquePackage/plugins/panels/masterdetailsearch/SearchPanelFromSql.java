package com.unigrative.pluginUniquePackage.plugins.panels.masterdetailsearch;

import com.evnt.client.common.EVEManager;
import com.evnt.common.MethodConst;
import com.evnt.eve.event.EVEvent;
import com.evnt.eve.event.InvalidTypeException;
import com.evnt.util.KeyConst;
import com.evnt.util.Money;
import com.evnt.util.Quantity;
import com.evnt.util.Util;
import com.fbi.fbdata.FBData;
import com.fbi.fbo.impl.dataexport.QueryRow;
import com.fbi.gui.combobox.ActiveComboBox;
import com.fbi.gui.combobox.FBComboBox;
import com.fbi.gui.combobox.FBConstComboBox;
import com.fbi.gui.rowdata.RowData;
import com.fbi.gui.table.FBTable;
import com.fbi.gui.table.FBTableColumnSettings;
import com.fbi.gui.table.FBTableModel;
import com.fbi.gui.textfield.FBTextFieldMoney;
import com.fbi.gui.textfield.FBTextFieldQuantity;
import com.fbi.gui.util.UtilGui;
import com.fbi.util.logging.FBLogger;
import com.jdc.components.JLookupComboBox;
import com.jdc.components.ListItem;
import com.jdc.components.NodeItem;
import com.jidesoft.combobox.AbstractComboBox;
import com.jidesoft.combobox.DateComboBox;
import com.jidesoft.combobox.ListComboBox;
import com.jidesoft.combobox.TreeComboBox;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.swing.JideButton;
import com.unigrative.pluginUniquePackage.plugins.util.sql.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.*;
import java.util.List;

public class SearchPanelFromSql<GenericRowData extends RowData, GenericFBData extends FBData> extends JPanel{
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchPanelFromSql.class);

    public static final int FILTER_TYPE_QUICK = 10;
    public static final int FILTER_TYPE_ADVANCED = 20;
    public static final String MAX_TABLE_ROWS = "MaxRows";
    public static final int DEFAULT_MAX_ROWS = 100;
    private FBTableModel<GenericRowData> tblModel;
    private Map<String, Object> quickFilterComponents = new HashMap();
    private Map<String, Object> advancedFilterComponents = new HashMap();
    private String sqlFileName;
    private SqlUtil sqlUtil;
    private EVEManager eveManager;
//    private String srcModuleName = "";
//    private String srcHandler = "searchResults";
//    private String destSearchModule;
//    private String destModuleHandler;
//    private int currBlock;
    private int currSearchType = 10;
//    private int maxDisplayItems = 100;
    private JScrollPane scrData;
    private FBTable tblData;
    private JPanel pnlNav;
//    private JideButton btnBack;
    private JideButton btnStatus;
//    private JideButton btnNext;
//    private JidePopup popupMaxRows;
//    private JPanel pnlMaxRows;
//    private JLabel lblMaxRows;
//    private FBTextFieldInt txtMaxRows;

    public SearchPanelFromSql() {
        this.initComponents();
        this.setMultiSelectMode(true);
//        UtilGui.makeComponentAutoSelect(this.txtMaxRows);
    }

    public int getCurrSearchType() {
        return this.currSearchType;
    }

    public void init(FBTableColumnSettings settings, EVEManager eveManager, String sqlFileName, SqlUtil sqlUtil) {
        this.eveManager = eveManager;
        this.sqlFileName = sqlFileName;
        this.sqlUtil = sqlUtil;
//        this.srcModuleName = srcModuleName;
//        this.srcHandler = srcHandler;
//        this.destSearchModule = method.getModuleName();
//        this.destModuleHandler = method.getMethodName();
        this.tblModel = new FBTableModel(this.tblData, settings);
        this.tblModel.setName(this.getName());
        this.loadGUISettings();
    }

//    public void setMaxDisplayItems(int maxDisplayItems) {
//        this.maxDisplayItems = maxDisplayItems;
//        this.txtMaxRows.setText(String.valueOf(maxDisplayItems));
//    }

//    public int getMaxDisplayItems() {
//        return this.maxDisplayItems;
//    }

//    public void makeBigger() {
//        Dimension buttonSize = new Dimension(32, 32);
//        this.btnNext.setMinimumSize(buttonSize);
//        this.btnNext.setMaximumSize(buttonSize);
//        this.btnNext.setPreferredSize(buttonSize);
//        this.btnNext.setIcon(new ImageIcon(this.getClass().getResource("/icon24/arrowsandnavigation/navigation1/blue/nav_right_blue.png")));
//        this.btnBack.setMinimumSize(buttonSize);
//        this.btnBack.setMaximumSize(buttonSize);
//        this.btnBack.setPreferredSize(buttonSize);
//        this.btnBack.setIcon(new ImageIcon(this.getClass().getResource("/icon24/arrowsandnavigation/navigation1/blue/nav_left_blue.png")));
//        this.tblData.setFont(new Font("Tahoma", 0, 15));
//        this.tblData.setRowHeight(32);
//    }

    public void loadGUISettings() {
        this.tblModel.loadGUISettings();
//        String rowCount = GUIProperties.getProperty(this.tblModel.getName() + "MaxRows", Integer.toString(this.maxDisplayItems));
//        this.maxDisplayItems = Integer.parseInt(rowCount);
//        this.currBlock = 0;
    }

    public void saveGUISettings() {
        this.tblModel.saveGUISettings();
//        this.saveRowCountGUISettings();
    }

//    private void saveRowCountGUISettings() {
//        GUIProperties.setProperty(this.tblModel.getName() + "MaxRows", Integer.toString(this.maxDisplayItems));
//    }

    public void setAutoResizeColumns(boolean autoResize) {
        if (autoResize) {
            this.getTblData().setAutoResizeMode(4);
        } else {
            this.getTblData().setAutoResizeMode(0);
        }

    }

    public void addKeyListener(KeyListener keyListener) {
        this.tblData.addKeyListener(keyListener);
    }

    public void executeSearch() {
        this.executeSearch(this.currSearchType, 0);
        this.saveGUISettings();
    }

    public void executeSearch(int filterType) {
        this.currSearchType = filterType;
        this.executeSearch();
    }

    private void executeSearch(int filterType, int currBlock) {
        this.currSearchType = filterType;
//        this.currBlock = currBlock;
        if (this.eveManager != null && this.eveManager.isRegistered()) {
            EVEvent event = this.eveManager.createRequest(MethodConst.RUN_DATA_EXPORT_QUERY);
//            event.add(KeyConst.START_INDEX, currBlock * this.maxDisplayItems);
//            event.add(KeyConst.RECORD_COUNT, this.maxDisplayItems);

            //ADD FIELD KEY DATA TO THE SQL STATEMENT AS PARAMETERS
//            List sqlParameters = new ArrayList();

            this.getSearchData(event, filterType); //Converts our filter list of components to a list of values for the sql parameters

            //LOAD SQL QUERY AND INSERT PARAMS

            String sql = sqlUtil.loadSql(sqlFileName, SqlUtil.paramsMapToWhereClause(event.getData()));
            LOGGER.debug(sql);

            event.add(KeyConst.DATA_EXPORT_QUERY, sql);
            this.checkEvent(this.eveManager.sendAndWait(event));
        }

    }

    private void getSearchData(EVEvent event, int filterType) {
        switch(filterType) {
            case 10:
            default:
                this.addKeys(this.quickFilterComponents, event);
                break;
            case 20:
                this.addKeys(this.advancedFilterComponents, event);
        }

    }

    private void addKeysJTextField(Object comp, String key, EVEvent event) {
        if (comp instanceof JFormattedTextField) {
            try {
                ((JFormattedTextField)comp).commitEdit();
            } catch (ParseException var6) {
            }

            Object ob = ((JFormattedTextField)comp).getValue();
            if (ob != null) {
                if (comp instanceof FBTextFieldMoney) {
                    Money m = ((FBTextFieldMoney)comp).getMoney();
                    if (!m.isZero()) {
                        event.add(key, m);
                    }
                } else if (comp instanceof FBTextFieldQuantity) {
                    Quantity q = ((FBTextFieldQuantity)comp).getQuantity();
                    if (!q.isZero()) {
                        event.add(key, q);
                    }
                } else if (ob instanceof Number) {
                    if (ob instanceof Integer) {
                        event.add(key, (Integer)ob);
                    } else if (ob instanceof Long) {
                        event.add(key, (Long)ob);
                    } else if (ob instanceof Double) {
                        event.add(key, (Double)ob);
                    } else {
                        event.add(key, ((Float)ob).floatValue());
                    }
                } else {
                    String s = ob.toString();
                    if (!Util.isEmpty(s)) {
                        event.add(key, s);
                    }
                }
            }
        } else if (comp instanceof FBComboBox) {
            int id = ((FBComboBox)comp).getSelectedID();
            if (id > 0) {
                event.add(key, id);
            }
        } else {
            String text = ((JTextField)comp).getText();
            if (!Util.isEmpty(text)) {
                event.add(key, text.trim());
            }
        }

    }

    private void addKeysDateComboBox(Object comp, String key, EVEvent event) {
        DateComboBox jCal = (DateComboBox)comp;
        Date date;
        Calendar cal;
        if (jCal.getName() != null && "toDate".equals(jCal.getName())) {
            cal = Calendar.getInstance();
            if (jCal.getDate() == null) {
                cal.set(1, 3000);
                cal.set(6, 1);
            } else {
                cal.setTime(jCal.getDate());
            }

            cal.set(11, 23);
            cal.set(12, 59);
            cal.set(13, 59);
            date = cal.getTime();
        } else if (jCal.getName() != null && "fromDate".equals(jCal.getName())) {
            cal = Calendar.getInstance();
            if (jCal.getDate() == null) {
                cal.set(1, 1000);
                cal.set(6, 1);
            } else {
                cal.setTime(jCal.getDate());
            }

            cal.set(11, 0);
            cal.set(12, 0);
            cal.set(13, 0);
            if (cal.get(1) != 1000) {
                cal.add(13, -1);
            }

            date = cal.getTime();
        } else {
            date = jCal.getDate();
        }

        if (date != null) {
            event.add(key, date);
        }

    }

    private void addKeysJComboBox(Object comp, String key, EVEvent event) {
        int active;
        if (comp instanceof JLookupComboBox) {
            active = ((JLookupComboBox)comp).getSelectedID();
            if (active > 0) {
                event.add(key, active);
            }
        } else if (comp instanceof FBComboBox) {
            active = ((FBComboBox)comp).getSelectedID();
            if (active > 0) {
                event.add(key, active);
            }
        } else if (comp instanceof ActiveComboBox) {
            active = ((ActiveComboBox)comp).getValue();
            if (active == 1) {
                event.add(key, true);
            } else if (active == -1) {
                event.add(key, false);
            }
        } else {
            Object selectedItem = ((JComboBox)comp).getSelectedItem();
            if (selectedItem != null) {
                if (selectedItem instanceof ListItem) {
                    int id = ((ListItem)selectedItem).getID();
                    event.add(key, id);
                } else {
                    event.addKeyObject(key, selectedItem);
                }
            }
        }

    }

    private void addKeys(Map map, EVEvent event) {
        Iterator var3 = map.entrySet().iterator();

        while(var3.hasNext()) {
            Object o = var3.next();
            Map.Entry e = (Map.Entry)o;
            String key = (String)e.getKey();
            Object comp = e.getValue();
            if (key != null && comp != null) {
                int id;
                if (comp instanceof FBComboBox) {
                    id = ((FBComboBox)comp).getSelectedID();
                    if (id > 0) {
                        event.add(key, id);
                    }
                } else if (comp instanceof JTextField) {
                    this.addKeysJTextField(comp, key, event);
                } else {
                    boolean isSelected;
                    if (comp instanceof JCheckBox) {
                        isSelected = ((JCheckBox)comp).isSelected();
                        event.add(key, isSelected);
                    } else if (comp instanceof JRadioButton) {
                        isSelected = ((JRadioButton)comp).isSelected();
                        event.add(key, isSelected);
                    } else if (comp instanceof JRadioButtonMenuItem) {
                        isSelected = ((JRadioButtonMenuItem)comp).isSelected();
                        event.add(key, isSelected);
                    } else if (comp instanceof DateComboBox) {
                        this.addKeysDateComboBox(comp, key, event);
                    } else if (comp instanceof JComboBox) {
                        this.addKeysJComboBox(comp, key, event);
                    } else if (comp instanceof AbstractComboBox) {
                        if (comp instanceof ListComboBox) {
                            id = ((ListComboBox)comp).getSelectedIndex();
                            if (id > 0) {
                                event.add(key, id);
                            }
                        } else if (comp instanceof TreeComboBox) {
                            TreePath path = (TreePath)((TreeComboBox)comp).getSelectedItem();
                            if (path != null) {
                                id = ((NodeItem)path.getLastPathComponent()).getId();
                                event.add(key, id);
                            }
                        } else {
                            String str = (String)((AbstractComboBox)comp).getSelectedItem();
                            event.add(key, str);
                        }
                    } else if (comp instanceof ArrayList) {
                        ArrayList list = (ArrayList)comp;
                        event.addObject(key, list);
                    } else if (comp instanceof FBData) {
                        FBData fbData = (FBData)comp;
                        event.add(key, fbData.getId());
                    } else if (comp instanceof RowData) {
                        RowData rowData = (RowData)comp;
                        event.add(key, rowData.getID());
                    } else if (comp instanceof FBConstComboBox) {
                        id = ((FBConstComboBox)comp).getSelectedID();
                        if (id > 0) {
                            event.add(key, id);
                        }
                    } else if (comp instanceof ButtonGroup) {
                        event.add(key, ((ButtonGroup)comp).getSelection().getActionCommand());
                    } else {
                        try {
                            event.add(key, comp);
                        } catch (InvalidTypeException var10) {
                            FBLogger.error(var10.getMessage(), var10);
                        }
                    }
                }
            }
        }

    }

    private void getSqlSearchData(EVEvent event, int filterType) {
        switch(filterType) {
            case 10:
            default:
                this.addKeysToListOfSqlParameters(this.quickFilterComponents, event);
                break;
//            case 20: TODO
//                this.addKeys(this.advancedFilterComponents, event);
        }

    }

    private void addKeysToListOfSqlParameters(Map map, EVEvent event) {
        Iterator var3 = map.entrySet().iterator();

        //EVEvent event = this.eveManager.createRequest(MethodConst.RUN_DATA_EXPORT_QUERY);
        //WE ARE DOING THIS SINCE ALL OF THE KEY METHODS EXPECT THIS PARAMETER
        //AT THE END WE WILL TAKE THE EVENT DATA MAP AND CONVERT IT TO A LIST

        while(var3.hasNext()) {
            Object o = var3.next();
            Map.Entry e = (Map.Entry)o;
            String key = (String)e.getKey();
            Object comp = e.getValue();
            if (key != null && comp != null) {
                int id;
                if (comp instanceof FBComboBox) {
                    id = ((FBComboBox)comp).getSelectedID();
                    if (id > 0) {
                        event.add(key, id);
                    }
                } else if (comp instanceof JTextField) {
                    this.addKeysJTextField(comp, key, event);
                } else {
                    boolean isSelected;
                    if (comp instanceof JCheckBox) {
                        isSelected = ((JCheckBox)comp).isSelected();
                        event.add(key, isSelected);
                    } else if (comp instanceof JRadioButton) {
                        isSelected = ((JRadioButton)comp).isSelected();
                        event.add(key, isSelected);
                    } else if (comp instanceof JRadioButtonMenuItem) {
                        isSelected = ((JRadioButtonMenuItem)comp).isSelected();
                        event.add(key, isSelected);
                    } else if (comp instanceof DateComboBox) {
                        this.addKeysDateComboBox(comp, key, event);
                    } else if (comp instanceof JComboBox) {
                        this.addKeysJComboBox(comp, key, event);
                    } else if (comp instanceof AbstractComboBox) {
                        if (comp instanceof ListComboBox) {
                            id = ((ListComboBox)comp).getSelectedIndex();
                            if (id > 0) {
                                event.add(key, id);
                            }
                        } else if (comp instanceof TreeComboBox) {
                            TreePath path = (TreePath)((TreeComboBox)comp).getSelectedItem();
                            if (path != null) {
                                id = ((NodeItem)path.getLastPathComponent()).getId();
                                event.add(key, id);
                            }
                        } else {
                            String str = (String)((AbstractComboBox)comp).getSelectedItem();
                            event.add(key, str);
                        }
                    } else if (comp instanceof ArrayList) {
                        ArrayList list = (ArrayList)comp;
                        event.addObject(key, list);
                    } else if (comp instanceof FBData) {
                        FBData fbData = (FBData)comp;
                        event.add(key, fbData.getId());
                    } else if (comp instanceof RowData) {
                        RowData rowData = (RowData)comp;
                        event.add(key, rowData.getID());
                    } else if (comp instanceof FBConstComboBox) {
                        id = ((FBConstComboBox)comp).getSelectedID();
                        if (id > 0) {
                            event.add(key, id);
                        }
                    } else if (comp instanceof ButtonGroup) {
                        event.add(key, ((ButtonGroup)comp).getSelection().getActionCommand());
                    } else {
                        try {
                            event.add(key, comp);
                        } catch (InvalidTypeException var10) {
                            FBLogger.error(var10.getMessage(), var10);
                        }
                    }
                }
            }
        }

        //COMPONENT MAP HAS BEEN CONVERTED TO A MAP OF KEYS/DATA
        //CONVERT IT TO A LIST UASBLE BY THE QUERY

//        event.getData().forEach((key, value) -> sqlParameters.add(value));
    }

    public void addFilterComponent(KeyConst key, Component comp) {
        this.addFilterComponent((KeyConst)key, comp, 10);
    }

    /** @deprecated */
    @Deprecated
    public void addFilterComponent(String key, Component comp) {
        this.addFilterComponent((String)key, comp, 10);
    }

    public void addFilterComponent(KeyConst key, Component comp, int filterType) {
        if (key != null && comp != null) {
            switch(filterType) {
                case 10:
                default:
                    this.quickFilterComponents.put(key.getString(), comp);
                    break;
                case 20:
                    this.advancedFilterComponents.put(key.getString(), comp);
            }
        }

    }

    /** @deprecated */
    @Deprecated
    public void addFilterComponent(String key, Component comp, int filterType) {
        if (key != null && comp != null) {
            switch(filterType) {
                case 10:
                default:
                    this.quickFilterComponents.put(key, comp);
                    break;
                case 20:
                    this.advancedFilterComponents.put(key, comp);
            }
        }

    }

    public void removeFilter(String key) {
        this.quickFilterComponents.remove(key);
        this.advancedFilterComponents.remove(key);
    }

    public void addFilter(KeyConst key, Object value, int filterType) {
        this.addFilter(key.getString(), value, filterType);
    }

    /** @deprecated */
    @Deprecated
    public void addFilter(String key, Object value, int filterType) {
        if (key != null && value != null) {
            switch(filterType) {
                case 10:
                default:
                    this.quickFilterComponents.put(key, value);
                    break;
                case 20:
                    this.advancedFilterComponents.put(key, value);
            }
        }

    }

    public void addFilter(KeyConst key, Object value) {
        this.addFilter(key.getString(), value);
    }

//    /** @deprecated */
//    @Deprecated
    //REMOVED DEPRECATION SINCE WE ARE CHANGING THE FUNCTIONALITY
    public void addFilter(String key, Object value) {
        this.addFilter((String)key, value, 10);
    }




    public void clearData() {
        if (this.tblModel != null) {
            this.tblModel.clearData();
        }

    }

    public void setSelectedID(int id) {
        if (this.tblModel != null) {
            this.tblModel.setSelectedID(id);
        }
    }

    public int getSelectedID() {
        return this.tblModel == null ? -1 : this.tblModel.getSelectedID();
    }

    public int[] getSelectedIDs() {
        return this.tblModel == null ? new int[0] : this.tblModel.getSelectedIDs();
    }

    public int getSelectedRowCount() {
        return this.tblModel == null ? 0 : this.tblModel.getSelectedRowsData().size();
    }

    public java.util.List<GenericRowData> getSelectedRowsData() {
        return (java.util.List)(this.tblModel == null ? new ArrayList() : this.tblModel.getSelectedRowsData());
    }

    public GenericRowData getSelectedRowData() {
        return this.tblModel == null ? null : this.tblModel.getSelectedRowData();
    }

    protected ArrayList<GenericRowData> getRowData(java.util.List<GenericFBData> dataList) {
        return new ArrayList();
    }

    private void updateUI(EVEvent event) {
        int[] selectedIDs = this.getSelectedIDs();

        //ORIGINAL METHODS TO GET THE SEARCH RESULTS FROM THE SERVER
        //THIS REQUIRES A METHOD IN THE BACKEND WHICH WE DONT HAVE
        //WE NEED TO CONVERT OUR QUERYROW RESULTS TO A LIST OF GenericFBData FROM OUR GenericDataObject
        List<QueryRow> searchResults = (ArrayList)event.getObject(KeyConst.DATA_EXPORT_RESULTS);

        //MAP ROWS TO OBJECTS
        java.util.List<GenericFBData> orgList = (List<GenericFBData>) Mappings.QueryRowToGenericDataObject(searchResults);


        List<GenericRowData> tableData = this.getRowData(orgList);
        this.tblModel.setTableData(tableData);
        int resultCount;
        if (event.contains(KeyConst.RESULT_COUNT)) {
            resultCount = event.getInt(KeyConst.RESULT_COUNT);
        } else {
            resultCount = tableData.size();
        }

//        int start = this.currBlock * this.maxDisplayItems;
//        if (start > resultCount) {
//            start = resultCount - start > 0 ? resultCount - start : 0;
//            this.currBlock = start / this.maxDisplayItems;
//        }
//
//        int end = start + this.maxDisplayItems;
//        if (end > resultCount) {
//            end = resultCount;
//        }
//
//        if (this.currBlock > 0) {
//            this.btnBack.setEnabled(true);
//            if (end == resultCount) {
//                this.btnNext.setEnabled(false);
//            } else {
//                this.btnNext.setEnabled(true);
//            }
//        } else {
//            this.btnBack.setEnabled(false);
//            if (end < resultCount) {
//                this.btnNext.setEnabled(true);
//            } else {
//                this.btnNext.setEnabled(false);
//            }
//        }
//
        if (resultCount == 0) {
            this.btnStatus.setText("No records found");
        }
        else{
            this.btnStatus.setText(resultCount + " Records");
        }
//
//        if (this.currSearchType == 20) {
//            this.btnStatus.setText(this.btnStatus.getText() + "*");
//            this.btnStatus.setToolTipText("Currently showing advanced search results.");
//        } else {
//            this.btnStatus.setToolTipText((String)null);
//        }

        this.tblModel.setSelectedIDs(selectedIDs);
    }

    private void checkEvent(EVEvent event) {
        if (event.getMessageType() == 201) {
            UtilGui.showMessageDialog("Could not show search results, try again later.");
        } else {
            this.updateUI(event);
        }

    }

    public SortableTable getTblData() {
        return this.tblData;
    }

    public FBTable getTable() {
        return this.tblData;
    }

    public FBTableModel<GenericRowData> getTblModel() {
        return this.tblModel;
    }

    public void setMultiSelectMode(boolean on) {
        if (on) {
            this.tblData.getSelectionModel().setSelectionMode(2);
        } else {
            this.tblData.getSelectionModel().setSelectionMode(0);
        }

    }

    private void tblDataMouseClicked(MouseEvent evt) {
        this.processMouseEvent(evt);
    }

//    private void btnBackActionPerformed() {
//        if (this.currBlock > 0) {
//            --this.currBlock;
//            this.executeSearch(this.currSearchType, this.currBlock);
//        }
//
//    }
//
//    private void btnStatusActionPerformed() {
//        this.popupMaxRows.setOwner(this.btnStatus);
//        if (!this.popupMaxRows.isPopupVisible()) {
//            this.txtMaxRows.setInt(this.maxDisplayItems);
//            this.popupMaxRows.showPopup();
//        } else {
//            this.popupMaxRows.hidePopup();
//        }
//
//    }
//
//    private void btnNextActionPerformed() {
//        ++this.currBlock;
//        this.executeSearch(this.currSearchType, this.currBlock);
//    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.tblData.setEnabled(enabled);
        this.pnlNav.setEnabled(enabled);
        this.btnStatus.setEnabled(enabled);
    }

//    private void popupMaxRowsPopupMenuWillBecomeInvisible() {
//        int maxRows = this.txtMaxRows.getInt();
//        if (maxRows > 0) {
//            this.maxDisplayItems = maxRows;
//            this.currBlock = 0;
//            this.saveRowCountGUISettings();
//            this.executeSearch();
//        }
//
//    }

//    private void txtMaxRowsKeyPressed(KeyEvent e) {
//        if (e.getKeyCode() == 10) {
//            this.popupMaxRows.hidePopup();
//        }
//
//    }

    private void initComponents() {
        this.scrData = new JScrollPane();
        this.tblData = new FBTable();
        this.pnlNav = new JPanel();
//        this.btnBack = new JideButton();
        this.btnStatus = new JideButton();
//        this.btnNext = new JideButton();
//        this.popupMaxRows = new JidePopup();
//        this.pnlMaxRows = new JPanel();
//        this.lblMaxRows = new JLabel();
//        this.txtMaxRows = new FBTextFieldInt();
        this.setName("this");
        this.setLayout(new BorderLayout());
        this.scrData.setName("scrData");
        this.tblData.setName("DBSearchtbl");
        this.tblData.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                SearchPanelFromSql.this.tblDataMouseClicked(e);
            }
        });
        this.scrData.setViewportView(this.tblData);
        this.add(this.scrData, "Center");
        this.pnlNav.setName("pnlNav");
        this.pnlNav.setLayout(new GridBagLayout());
        ((GridBagLayout)this.pnlNav.getLayout()).columnWidths = new int[]{0, 0, 0, 0};
        ((GridBagLayout)this.pnlNav.getLayout()).rowHeights = new int[]{0, 0};
        ((GridBagLayout)this.pnlNav.getLayout()).columnWeights = new double[]{0.0D, 1.0D, 0.0D, 1.0E-4D};
        ((GridBagLayout)this.pnlNav.getLayout()).rowWeights = new double[]{0.0D, 1.0E-4D};
//        this.btnBack.setIcon(new ImageIcon(this.getClass().getResource("/icon16/arrowsandnavigation/navigation1/blue/nav_left_blue.png")));
//        this.btnBack.setToolTipText("back");
//        this.btnBack.setName("DBSearchBackbtn");
//        this.btnBack.setPreferredSize(new Dimension(21, 21));
//        this.btnBack.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                SearchPanelFromSql.this.btnBackActionPerformed();
//            }
//        });
//        this.pnlNav.add(this.btnBack, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(0, 0, 0, 2), 0, 0));
        this.btnStatus.setText("No records found");
        this.btnStatus.setBorder(new EmptyBorder(3, 3, 3, 3));
        this.btnStatus.setName("DBSearchrecordcountbtn");
        this.btnStatus.setOpaque(false);
//        this.btnStatus.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                SearchPanelFromSql.this.btnStatusActionPerformed();
//            }
//        });
        this.pnlNav.add(this.btnStatus, new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(1, 0, 0, 2), 0, 0));
//        this.btnNext.setIcon(new ImageIcon(this.getClass().getResource("/icon16/arrowsandnavigation/navigation1/blue/nav_right_blue.png")));
//        this.btnNext.setToolTipText("next");
//        this.btnNext.setName("DBSearchForwardbtn");
//        this.btnNext.setPreferredSize(new Dimension(21, 21));
//        this.btnNext.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                SearchPanelFromSql.this.btnNextActionPerformed();
//            }
//        });
//        this.pnlNav.add(this.btnNext, new GridBagConstraints(2, 0, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.add(this.pnlNav, "South");
//        this.popupMaxRows.setOwner(this.btnStatus);
//        this.popupMaxRows.setDefaultFocusComponent(this.txtMaxRows);
//        this.popupMaxRows.setName("popupMaxRows");
//        this.popupMaxRows.addPopupMenuListener(new PopupMenuListener() {
//            public void popupMenuCanceled(PopupMenuEvent e) {
//            }
//
//            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
//                SearchPanelFromSql.this.popupMaxRowsPopupMenuWillBecomeInvisible();
//            }
//
//            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
//            }
//        });
//        this.popupMaxRows.setLayout(new FlowLayout());
//        this.pnlMaxRows.setName("pnlMaxRows");
//        this.pnlMaxRows.setLayout(new GridBagLayout());
//        ((GridBagLayout)this.pnlMaxRows.getLayout()).columnWidths = new int[]{0, 98, 0};
//        ((GridBagLayout)this.pnlMaxRows.getLayout()).rowHeights = new int[]{0, 0};
//        ((GridBagLayout)this.pnlMaxRows.getLayout()).columnWeights = new double[]{0.0D, 0.0D, 1.0E-4D};
//        ((GridBagLayout)this.pnlMaxRows.getLayout()).rowWeights = new double[]{0.0D, 1.0E-4D};
//        this.lblMaxRows.setText("Max Rows:");
//        this.lblMaxRows.setName("lblMaxRows");
//        this.pnlMaxRows.add(this.lblMaxRows, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(0, 0, 0, 5), 0, 0));
//        this.txtMaxRows.setName("txtMaxRows");
//        this.txtMaxRows.addKeyListener(new KeyAdapter() {
//            public void keyPressed(KeyEvent e) {
//                SearchPanelFromSql.this.txtMaxRowsKeyPressed(e);
//            }
//        });
//        this.pnlMaxRows.add(this.txtMaxRows, new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
//        this.popupMaxRows.add(this.pnlMaxRows);
    }
}
