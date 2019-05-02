//we need this package to gain access to protected methods in the SO module

package com.evnt.client.modules.so;

import com.evnt.client.common.EVEManager;
import com.evnt.client.common.EVEManagerUtil;
import com.evnt.client.modules.so.dialogs.DlgInvShort;
import com.evnt.common.MethodConst;
import com.evnt.common.data.soitem.SOItem;
import com.evnt.eve.event.EVEvent;
import com.evnt.eve.modules.CustomFieldModule;
import com.evnt.ui.OptionMessage;
import com.evnt.util.KeyConst;
import com.evnt.util.Money;
import com.evnt.util.Quantity;
import com.evnt.util.Util;
import com.fbi.fbdata.part.PartFpo;
import com.fbi.fbdata.product.ProductFpo;
import com.fbi.fbo.CustomField;
import com.fbi.fbo.impl.dataexport.QueryRow;
import com.fbi.gui.util.UtilGui;
import com.fbi.plugins.FishbowlPluginButton;
import com.fbi.sdk.UOMUtil;
import com.fbi.sdk.constants.*;
import com.fbi.util.FbiException;
import com.fbi.util.logging.FBLogger;
import jpos.JposException;
import jpos.Scanner;
import jpos.config.JposEntry;
import jpos.config.simple.SimpleEntry;
import jpos.config.simple.SimpleEntryRegistry;
import jpos.config.simple.xml.SimpleXmlRegPopulator;
import jpos.events.DataEvent;
import jpos.events.DataListener;
import jpos.events.ErrorEvent;
import jpos.events.ErrorListener;
import jpos.loader.JposServiceConnection;
import jpos.loader.JposServiceLoader;
import jpos.loader.JposServiceManager;
import jpos.services.BaseService;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.io.StringReader;
import java.util.*;
import java.util.List;
import java.util.Timer;


public class ScannerButton
        extends FishbowlPluginButton implements DataListener, ErrorListener {
    private static final Logger LOGGER = FBLogger.getLogger();//ScannerButton.class);
    private SOModuleClient _SOModuleClient;
    EVEManager eveManager = EVEManagerUtil.getEveManager();
    private List<CustomField> soItemCustomFieldList;


    private boolean isDebug; //Set after we scan until FB is done processing; //TODO: look at finishedAdding bool in add method

    /////BARCODE SCANNER OBJECTS
    private Scanner scanner = null;
    JposServiceConnection serviceConnection;
    BaseService service;
    int LI2478_id;


    public ScannerButton() {
        this("Sales Order");

    }

    protected ScannerButton(String moduleName) {
        this.setModuleName(moduleName);
        this.setPluginName(SOAddonsPlugin.MODULE_NAME);
        this.setIcon((Icon) new ImageIcon(this.getClass().getResource("/images/barcode-scanner24x24.png")));
        this.setText("Scanner");

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

//                if (!isEligibleForAdd()) {
//                    //no SO loaded
//                    JOptionPane.showMessageDialog(null, "No Sales Order Open"); //returns the SOID
//                } else {
                    //TODO: SCANNER SETTING WINDOW
                    final int result = UtilGui.showConfirmDialog("Reset the scanner?","Scanner reset", 0);
                    if (result == 0){
                        resetScanner();
                    }
//                }

            }
        });

        _SOModuleClient = (SOModuleClient)SOAddonsPlugin.getInstance().getModule("Sales Order");
        initScanner();

    }

    protected ScannerButton(boolean isDebug){
        this.isDebug = isDebug;
    }

    public static void main(String[] args) {
        ScannerButton button = new ScannerButton(true);
//        System.setProperty(JposPropertiesConst.JPOS_POPULATOR_FILE_URL_PROP_NAME, AddItemButton.class.getResource("jpos.xml").toString());
        //System.setProperty(JposPropertiesConst.JPOS_POPULATOR_FILE_URL_PROP_NAME, "C:\\Users\\bnordstrom\\Source\\jpos.xml" );
        //button.isDebug = true;
        button.initScanner();

        System.out.println(button.getScanners());

        System.out.println(button.getCorrectScannerID());
        //get the scanner list
        //parse xml
        //save the scanner id to the DB? need to set client specific in file


    }

    public void startTimer(){
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                addItemToSO(636);
            }
        };
        Timer timer = new Timer("Timer");

        long delay  = 3000L;
        long period = 5000L;
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }




    ///////BARCODE SCANNER METHODS
    public void initScanner() {

        //System.setProperty("jpos.config.populatorFile", "/images/jpos.xml");
        //System.setProperty(JposPropertiesConst.JPOS_POPULATOR_FILE_PROP_NAME,  "C:\\Users\\bnordstrom\\Source\\jpos.xml" );
        //LOGGER.error(CheckoutButton.class.getResource("jpos.xml").toString());


        //System.setProperty(JposPropertiesConst.JPOS_POPULATOR_FILE_URL_PROP_NAME, "file:/C:/Users/bnordstrom/Source/jpos.xml");

        //System.setProperty("jpos.config.regPopulatorClass","jpos.config.simple.xml.SimpleXmlRegPopulator");
        //System.setProperty("jpos.loader.serviceManagerClass","jpos.loader.simple.SimpleServiceManager");

        //System.setProperty("jpos.util.tracing.TurnOnNamedTracers", "JposServiceLoader,SimpleEntryRegistry,SimpleRegPopulator,XercesRegPopulator");
        //System.setProperty("jpos.util.tracing.TurnOnAllNamedTracers", "ON");

        ///////SETUP FOR THE CHECKOUT BARCODE SCANNER


        try {

            //LOGGER.error("System property: " + System.getProperty(JposPropertiesConst.JPOS_POPULATOR_FILE_URL_PROP_NAME));

            //JOptionPane.showMessageDialog(null, makeScannerTable());

            //UtilGui.showMessageDialog(makeScannerTable(), "scanners", 1);


            //LOGGER.error("After scanner Table servicemanager instance url: " + manager.getEntryRegistry().getRegPopulator().getEntriesURL());
            String logicalDeviceName = "ZebraAllScanners";
            try {
                JposServiceManager manager = JposServiceLoader.getManager();
                //LOGGER.error("manager url path: " + manager.getRegPopulator().getEntriesURL().getPath());
                //JposEntryRegistry registry = JposServiceLoader.getManager().getEntryRegistry();

                SimpleEntry entry = new SimpleEntry("ZebraAllScanners", manager.getRegPopulator());

                entry.addProperty("serviceInstanceFactoryClass", "com.motorola.jpos.service.scanner.SymScannerSvc112Factory");
                entry.addProperty("serviceClass", "com.motorola.jpos.service.scanner.SymScannerSvc112");
                entry.addProperty("vendorName", "Zebra Technologies");
                entry.addProperty("vendorURL", "https://www.zebra.com");
                entry.addProperty("deviceCategory", "Scanner");
                entry.addProperty("jposVersion", "1.12");
                entry.addProperty("productDescription", "Zebra Serial/USB Scanner");
                entry.addProperty("productName", "Zebra Scanner");
                entry.addProperty("productURL", "https://www.zebra.com");
                entry.addProperty("ScannerType", "1");
                entry.addProperty("ExclusiveClaimLevel", "0");

                manager.getEntryRegistry().addJposEntry((JposEntry) entry);

                //LOGGER.error("Manager registry size: " + manager.getEntryRegistry().getSize());
            } catch (Exception e) {
                LOGGER.error("Error with manager init: ", e);
            }


//
//            try {
//                this.serviceConnection = JposServiceLoader.findService(logicalDeviceName);
//            } catch (JposException var12) {
//                throw var12;
//            } catch (Exception var13) {
//                throw new JposException(109, "Device profile not found", var13);
//            }
//
//            try {
//                this.serviceConnection.connect();
//            } catch (JposException var10) {
//                throw var10;
//            } catch (Exception var11) {
//                throw new JposException(104, "Could not connect to service", var11);
//            }
//
//            try {
//                this.service = (BaseService)this.serviceConnection.getService();
//            } catch (Exception var9) {
//                throw new JposException(104, "Could not get service instance", var9);
//            }
//
//            JposException jposException = null;
//            boolean bRealOpenSucceeded = false;


//            try {
//                EventCallbacks callbacks = this.createEventCallbacks();
//                this.service.open(logicalDeviceName, callbacks);
//                bRealOpenSucceeded = true;
//                this.serviceVersion = this.service.getDeviceServiceVersion();
//                this.setDeviceService(this.service, this.serviceVersion);
//                this.bOpen = true;
//            } catch (JposException var7) {
//                jposException = var7;
//            } catch (Exception var8) {
//                var8.printStackTrace();
//                jposException = new JposException(111, "Unhandled exception from Device Service", var8);
//            }
//
//            if (!this.bOpen) {
//                try {
//                    this.service.close();
//                } catch (Exception var6) {
//                    ;
//                }
//
//                try {
//                    this.serviceConnection.disconnect();
//                } catch (Exception var5) {
//                    ;
//                }
//
//                this.serviceConnection = null;
//                this.serviceVersion = 0;
//                throw jposException;
//            }


            //manager.createConnection("ZebraAllScanners");

            //JCoreScanner jCoreScanner = new JCoreScanner();
//            try {
//                Class.forName( "com.motorola.jpos.service.scanner.SymScannerSvc19" );
//                UtilGui.showMessageDialog("Class 19 found");
//            } catch( ClassNotFoundException e ) {
//                UtilGui.showMessageDialog("Class 19 not found");
//            }
//            try {
//                Class.forName( "com.motorola.jpos.service.scanner.SymScannerSvc112" );
//                //JOptionPane.showMessageDialog(null,"Class 112 found");
////
////
////                JposEntryRegistry registry = JposServiceLoader.getManager().getEntryRegistry();
////                SimpleEntry entry = new SimpleEntry( "ZebraAllScanners", new SimpleXmlRegPopulator());
////
////
////                entry.addProperty("serviceInstanceFactoryClass", "com.motorola.jpos.service.scanner.SymScannerSvc112Factory");
////                entry.addProperty("serviceClass", "com.motorola.jpos.service.scanner.SymScannerSvc112");
////                entry.addProperty("vendorName", "Zebra Technologies");
////                entry.addProperty("vendorURL", "https://www.zebra.com");
////                entry.addProperty("deviceCategory", "Scanner");
////                entry.addProperty("jposVersion", "1.12");
////                entry.addProperty("productDescription", "Zebra Serial/USB Scanner");
////                entry.addProperty("productName", "Zebra Scanner");
////                entry.addProperty("productURL", "https://www.zebra.com");
////                entry.addProperty("ScannerType", "1");
////                entry.addProperty("ExclusiveClaimLevel", "0");
////
////
////
////                registry.addJposEntry((JposEntry)entry);
////                registry.load();
////                LOGGER.error("registry size" + registry.getSize());
//
//
//            } catch( ClassNotFoundException e ) {
//                UtilGui.showMessageDialog("Class 112 not found");
//            }

            scanner = new Scanner();
            scanner.addDataListener(this);
            scanner.addErrorListener(this);


            scanner.open("ZebraAllScanners");
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Failed to open \"" + "ZebraAllScanners" + "\"\nException: " + e.getMessage(), "Failed", JOptionPane.ERROR_MESSAGE);
            LOGGER.error("Jpos exception on open ", e);
        }


        try {

            if (!scanner.getClaimed()) {

                //_SOModuleClient = SOAddonsPlugin.getInstance().getModule("Sales Order");

                //not claimed yet so claim
                try {
                    scanner.claim(1000);

                    scanner.setDeviceEnabled(true);
                    scanner.setDataEventEnabled(true);
                    scanner.setDecodeData(true);
                    scanner.setAutoDisable(false);

                    //set the correct id for the direct IO commands
                    //this.LI2478_id = this.getCorrectScannerID();
                    DirectIOCommands.scannerID = this.getCorrectScannerID();


                    //if (isDebug){
                        LOGGER.info("correct scanner ID: " + DirectIOCommands.scannerID );
                    //}

                } catch (JposException e) {
                    //JOptionPane.showMessageDialog(null, "Failed to claim \"" + "ZebraAllScanners" + "\"\nException: " + e.getMessage(), "Failed", JOptionPane.ERROR_MESSAGE);
                    //TODO: Swallow error so it works on computers without the device?
                    //LOGGER.error("Jpos exception on claim ", e);

                }

                //TODO: if claimed success, change button color?
//
//                try {
//                    UtilGui.showMessageDialog(scanner.getPhysicalDeviceName());
//
//                    if (scanner.getState() != JposConst.JPOS_S_CLOSED) {
//                        String ver;
//                        ver = Integer.toString(scanner.getDeviceServiceVersion());
//                        String msg = "Service Description: " + scanner.getDeviceServiceDescription();
//                        msg = msg + "\nService Version: v" + new Integer(ver.substring(0, 1)) + "." + new Integer(ver.substring(1, 4)) + "." + new Integer(ver.substring(4, 7));
//                        ver = Integer.toString(scanner.getDeviceControlVersion());
//                        msg += "\n\nControl Description: " + scanner.getDeviceControlDescription();
//                        msg += "\nControl Version: v" + new Integer(ver.substring(0, 1)) + "." + new Integer(ver.substring(1, 4)) + "." + new Integer(ver.substring(4, 7));
//                        msg += "\n\nPhysical Device Name: " + scanner.getPhysicalDeviceName();
//                        msg += "\nPhysical Device Description: " + scanner.getPhysicalDeviceDescription();
//
//                        msg += "\n\nProperties:\n------------------------";
//                        msg += "\nCapPowerReporting: " + (scanner.getCapPowerReporting() == JposConst.JPOS_PR_ADVANCED ? "Advanced" : (scanner.getCapPowerReporting() == JposConst.JPOS_PR_STANDARD ? "Standard" : "None"));
//                        JOptionPane.showMessageDialog(null, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
//                    }
//                } catch (JposException e) {
//                    JOptionPane.showMessageDialog(null, "Exception in Info\nException: " + e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
//                    System.err.println("Jpos exception " + e);
//                }
            }
        } catch (JposException e) {
            //JOptionPane.showMessageDialog(null, "Failed to check claim \"" + "ZebraAllScanners" + "\"\nException: " + e.getMessage(), "Failed", JOptionPane.ERROR_MESSAGE);
            LOGGER.error("Jpos exception on check claim in init ", e);
        }
        LOGGER.info("Ready");

    }

    @Override
    public void dataOccurred(DataEvent dataEvent) {

//        LOGGER.error("modified right at beginning of data event: " + _SOModuleClient.getController().isModified());


        String upc = null;

        if(!isDebug) {
//            try {
//                LOGGER.error(_SOModuleClient.so.getSoFpo().getNum());
//            }
//            catch (Exception e){
//                LOGGER.error("error", e);
//            }
            //get upc first
            try {

                Scanner scn = (Scanner) dataEvent.getSource();
                if (scn.equals(scanner)) {
                    upc = new String(scanner.getScanData());
                } else {
                    //scn.setDataEventEnabled(true);
                    upc = new String(scn.getScanData());
                    LOGGER.info("From scn object: " + new String(scn.getScanData()));
                }

                //UtilGui.showMessageDialog( "Scanned data: " + upc);
                LOGGER.info("Scan data: " + upc);


                scanner.setDeviceEnabled(false); //disable between scans to keep from jamming up
            } catch (JposException je) {
                LOGGER.error("Scanner: dataOccurred: Jpos Exception" + je);
            }

            //check for RCL
            //check the data returned
            //if it is a RCL_ event, then open the SO module and recall the order
            //else add product to order
            if (upc.substring(0,4).equals("RCL_")){
//                    this._SOModuleClient.showModule("Sales Order");
                    SOAddonsPlugin.getInstance().showModule("Sales Order");
                    LOGGER.error("SO NUM: " + upc.substring(4));
                    this._SOModuleClient.loadSO(upc.substring(4));


            }
            else {
                //product scanned, check SO state
                if (!isEligibleForAdd()) {

                    //LOGGER.error(AddItemButton.this.getObjectId());
                    actionScannerError();

                    JOptionPane.showMessageDialog(null, "Check the current open salesorder order"); //returns the SOID

                } else {

                    _SOModuleClient.getController().setModified(false);

                    //load product
                    int productID = getProductID(upc);
                    LOGGER.info("Product ID = " + productID);


                    if (productID != 0) {
                        LOGGER.info("Adding item to SO");
                        addItemToSO(productID);
                    } else {
                        LOGGER.info("Product not found");
                        actionScannerError();

                        UtilGui.showMessageDialog("Product Not found");
                    }
                    //renable the scanner - moved below
//                    try {
//                        scanner.setDataEventEnabled(true);
//                        scanner.setDeviceEnabled(true);
//                    } catch (JposException je) {
//                        LOGGER.error("Scanner: dataOccurred: Jpos Exception" + je);
//                    }
                }
            }

            try {
                scanner.setDataEventEnabled(true);
                scanner.setDeviceEnabled(true);

            } catch (JposException je) {
                LOGGER.error("Scanner: dataOccurred: Jpos Exception" + je);
            }
        }
        else {
            try {

                Scanner scn = (Scanner) dataEvent.getSource();
                if (scn.equals(scanner)) {
                    //if (autoDataEventEnableCB.isSelected()) {
                    scanner.setDataEventEnabled(true);
                    //}
                    upc = new String(scanner.getScanData());

                } else {
                    scn.setDataEventEnabled(true);
                    upc = new String(scn.getScanData());
                    System.err.println( "From scn object: " + new String(scn.getScanData()));
                }
                scanner.setDeviceEnabled(true); //renable device after scan TODO: maybe move this to after the FB product load

                //JOptionPane.showMessageDialog(null, "Scanned data: " + upc);
                System.err.println("Scan data: " + upc);


            } catch (JposException je) {
                System.err.println("Scanner: dataOccurred: Jpos Exception" + je);
            }

            int productID = 0;
            System.err.println("Product ID = " + productID);


            if (productID != 0) {
                System.err.println("Adding item to SO");
                addItemToSO(productID);
            } else {
                System.err.println("Product not found");
                //TODO: beep error
                actionScannerError();
            }


        }
    }

    @Override
    public void errorOccurred(ErrorEvent errorEvent) {
        LOGGER.error("Error in scanner: " + errorEvent.getErrorCode() );
    }

    public void actionScannerError(){

        setScannerAction(DirectIOCommands.XML_SET_RED_LED_ON());
        setScannerAction(DirectIOCommands.XML_SET_ERROR_BEEP());
        setScannerAction(DirectIOCommands.XML_SET_RED_LED_OFF());
    }

    public String getScanners(){
        int[] directIOStatus = new int[1];
        directIOStatus[0] = -1;
        StringBuffer inOutXml = new StringBuffer();
//        inOutXml.append(xmlCommand);

        try {
            scanner.directIO(1, directIOStatus, inOutXml);
        } catch (JposException ex) {
            LOGGER.error("Jpos exception: " + ex.getMessage() + " " + ex.getOrigException());
        }

        return inOutXml.toString();
    }

    public int getCorrectScannerID(){
        Scanners scannerList = new Scanners();

        try {
            JAXBContext jc = JAXBContext.newInstance(Scanners.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();

            scannerList = (Scanners)unmarshaller.unmarshal(new StringReader(getScanners()));
        }
        catch (JAXBException e){
            LOGGER.error("Error binding to scanner class for XML parse ", e);
        }

        for (com.unigrative.plugins.salesorder.Models.Scanner scanner: scannerList.getScanners()
             ) {
            if (scanner.getModelnumber().substring(0,6).equals("LI4278")){
                return scanner.getScannerID();
            }
        }

        return 0;
    }

    public void setScannerAction(String xmlCommand){
        int[] directIOStatus = new int[1];
        directIOStatus[0] = -1;
        StringBuffer inOutXml = new StringBuffer();
                inOutXml.append(xmlCommand);

        try {
            scanner.directIO(5004, directIOStatus, inOutXml);
        } catch (JposException ex) {
            LOGGER.error("Jpos exception: " + ex.getMessage() + " " + ex.getOrigException());
        }
        if(directIOStatus[0] == 0 && !(inOutXml.toString().equals("null")))
        {
            //RESPONSE
            System.err.println("Response: \r\n" + inOutXml.toString());
            //jtxtDIOoutXml.setText(inOutXml.toString());
        }else
        {
            System.err.println("DirectIOStatus: " + directIOStatus[0]);
            //LOGGER.error("Problem setting action");
        }
    }

    private void resetScanner(){
        if (scanner != null){
            try {
                scanner.release();
            }
            catch (JposException e){
                LOGGER.error("Error releasing scanner", e);
            }
            try {
                scanner.close();
            }
            catch (JposException e){
                LOGGER.error("Error closing scanner", e);
            }




        }
        //re-initalize
        initScanner();
        UtilGui.showMessageDialog("Scanner Reset");
    }


    public static String defaultLogicalScanner = "defaultScanner";
    public JPanel makeScannerTable() {

        JPanel mainPanel = new JPanel(false);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(Box.createVerticalStrut(20));

        JLabel label = new JLabel("Below is a list of the devices found in your jpos.xml file.");
        mainPanel.add(label);
        try {
            SimpleXmlRegPopulator populator = new SimpleXmlRegPopulator();
            LOGGER.error(populator.getClassName());
//            LOGGER.error("Last error: " + populator.getLastLoadException().getMessage());
            LOGGER.error("Entries URL path: " + populator.getEntriesURL().getPath());
            LOGGER.error("UniqueID: " + populator.getUniqueId());

            System.err.println(populator.getClassName());
            //System.err.println("Last error: " + populator.getLastLoadException() != null ? populator.getLastLoadException().getMessage() : "");
            System.err.println("Entries URL path: " + populator.getEntriesURL().getPath());
            System.err.println("UniqueID: " + populator.getUniqueId());

            //SimpleEntryRegistry reg = new SimpleEntryRegistry(new SimpleXmlRegPopulator());
            SimpleEntryRegistry reg = new SimpleEntryRegistry(populator);
            reg.load();

            LOGGER.error("Entries URL path after registry load: " + populator.getEntriesURL().getPath());

            String[] colNames = {"Category", "Logical Name", "Vendor", "Product Name"};

            Object[][] data = new Object[reg.getSize()][4];
            String logName;
            Enumeration entriesEnum = reg.getEntries();




            int count = 0;
            while (entriesEnum.hasMoreElements()) {
                JposEntry entry = (JposEntry) entriesEnum.nextElement();

                Enumeration propEnum = entry.getPropertyNames();
                String props = null;
                while (propEnum.hasMoreElements()) {
                    //JposEntry.Prop prop =  (JposEntry.Prop)propEnum.nextElement();
                    //LOGGER.error("Prop Name: " + prop.getName() + " Value: " + prop.getValueAsString());
                    String propName = (String)propEnum.nextElement();
                    LOGGER.error("Name: " + propName + " Value: " + entry.getProp(propName).getValueAsString());

                }



                Object[] row = {entry.getProp(JposEntry.DEVICE_CATEGORY_PROP_NAME).getValueAsString(),
                        entry.getLogicalName(),
                        entry.getProp(JposEntry.VENDOR_NAME_PROP_NAME).getValueAsString(),
                        entry.getProp(JposEntry.PRODUCT_NAME_PROP_NAME).getValueAsString()};
                data[count] = row;
                logName = (String) row[0];
                if (defaultLogicalScanner.equalsIgnoreCase("defaultScanner") && logName.equalsIgnoreCase("Scanner")) {
                    defaultLogicalScanner = "ZebraAllScanners";//(String) row[1];
                }
                count++;
            }

            JTable table = new JTable(data, colNames);
            JScrollPane scrollPane = new JScrollPane(table);
            table.setPreferredScrollableViewportSize(new Dimension(700, 200));

            //Adjust the column widths
            //This is really kudgy, but it seems to work (a little)
            TableColumn column = null;
            int maxsize;
            int size;

            for (int i = 0; i < 4; i++) {
                column = table.getColumnModel().getColumn(i);
                maxsize = 0;
                for (int j = 0; j < count; j++) {
                    size = ((String) data[j][i]).length();
                    if (maxsize < (size * 5)) {
                        maxsize = size * 5;
                    }
                }
                column.setPreferredWidth(maxsize);
            }

            System.err.println("Entries URL path: " + populator.getEntriesURL().getPath());
            LOGGER.error("Entries URL path: " + populator.getEntriesURL().getPath());

            mainPanel.add(scrollPane);
        }
        catch (Exception e){
            LOGGER.error("error", e);
        }
        return mainPanel;
    }

    /////////ADD BUTTON METHODS

    private int getProductID(String upc) {
        //repository to get productID
        StringBuilder query = new StringBuilder("Select product.id ");
        query.append("from product ");
        query.append("WHERE (UPPER(num) = UPPER('").append(upc).append("') ");
        query.append("OR Upc = '").append(upc).append("') ");
        List stateRows = this.runQuery(query.toString());
        if (!Util.isEmpty((List) stateRows)) {
            return ((QueryRow) stateRows.get(0)).getInt("id");
        }
        return 0;
    }

    private void addItemToSO(int productId) {
        //testing we are using 100Gcl 636



        //LOGGER.error(_SOModuleClient.getModuleName());

//_SOModuleClient.getController().setModified(false);
        //LOGGER.error("Module is modified :" + _SOModuleClient.getController().isModified());
        try {
            //get current so object to add our item to
//            LOGGER.error("From addItem");
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    LOGGER.error("Starting thread");
                    if (addProductToSO(productId, new Quantity(1), null, true) == null) {
                        //error on adding
                        LOGGER.error("Error adding item to SO");
                    }
//                }
//            }).start();

            //_SOModuleClient.saveSO(false,false);
        } catch (Exception e) {
            LOGGER.error("Error adding item", e);
        }
    }

    private boolean isEligibleForAdd(){

        if (_SOModuleClient.getObjectId() == -1){
            //could be no SO loaded or a new one thats not saved
            if (_SOModuleClient.so == null){
                return false; //no so loaded
            }
        }
        else{
            if (_SOModuleClient.so.getStatus() != SOStatusConst.ISSUED.getId() &&
                    _SOModuleClient.so.getStatus() != SOStatusConst.IN_PROGRESS.getId() &&
                    _SOModuleClient.so.getStatus() != SOStatusConst.ESTIMATE.getId()){
                //UtilGui.showMessageDialog("Status no matchy");
                return false; //these are closed orders
            }
            else {
                //UtilGui.showMessageDialog("Status matchy");
                return true; //new SO that's not saved yet.
            }
        }
        return true;

    }

    ////// COPIED METHODS FROM EXISTING MODULES, CHECK ON FB UPDATE THAT THESE ARE THE SAME
    private SOItem addProductToSO(final int productId, Quantity qty, final Money price, final boolean changeQty) {
//        if (_SOModuleClient.so.== null){
//            LOGGER.error("SO is null");
//        }
//        else{
//            LOGGER.error(_SOModuleClient.so.getCustomerFPO().getName());
//        }

        SOItem soItem = null;
        final DlgInvShort dlgInvShort = new DlgInvShort();
        //LOGGER.error("Module is modified :" + _SOModuleClient.getController().isModified());
        //_SOModuleClient.getController().setModified(false);
        //LOGGER.error("Module is modified :" + _SOModuleClient.getController().isModified());
        if (productId > 0 && qty.greaterThan(0)) {
            try {
                boolean addCorePart = true;
                boolean finishedAdding = false;
                ProductFpo productFPO = null;

                while (!finishedAdding) {
                    soItem = _SOModuleClient.so.createSOItem();
                    soItem.setCustomFieldList((List) SerializationUtils.clone((Serializable) (ArrayList) this.soItemCustomFieldList));
                    soItem.setProductID(productId, _SOModuleClient.so.getSoFpo().getCustomerId());
                    //this checks if the qty entered fits into the uom selected. We are always doing 1 so we dont need this
//                    if (!checkSOItemQtyAndUOM(qty, soItem.getUom())) {
//                        return null;
//                    }
                    if (price != null) {
                        soItem.setUnitPrice(price);
                        soItem.getSalesOrderItemFpo().setTotalPrice(price);
                    }
                    if (SystemPropertyConst.CUSTOM_FIELD_PRODUCT_TO_SO_ITEM.getBoolean()) {
                        this.mapCustomFields(productId, soItem);
                    }
                    final PartFpo partFpo = soItem.getPartFpo();
                    productFPO = soItem.getProductFpo();
                    if (productFPO.isKitFlag()) {
                        //WE DONT WANT TO HANDLE KITS AT THIS POINT
                        //SOUND ERROR AND PROMPT TO ENTER MANUALLY
                        actionScannerError();
                        UtilGui.showMessageDialog("Scanner cannot process kits. Add product " + soItem.getProductFpo().getNum() +" manually");
//                        this.showAddSOItemWizard(soItem, qty, false, true);
//                        this.pnlQAProduct.setSelectedID(-1);
//                        this.quantityFormattedTextField.setQuantity(Quantity.ONE);
//                        this.quantityFormattedTextField.requestFocus();
                        finishedAdding = true;
                    } else {
                        if (soItem.getType() == SOItemTypeConst.SALE || soItem.getType() == SOItemTypeConst.SHIPPING) {
                            //TODO: IF PART INV IS SHORT, PLAY ERROR SOUND ON SCANNER TO GET ATTENTION

                            Quantity partQty = qty;
                            if (soItem.getSalesOrderItemFpo().getUomId() != partFpo.getUomId()) {
                                partQty = UOMUtil.convertQty(qty, soItem.getSalesOrderItemFpo().getUomId(), partFpo.getUomId());
                            }

                            //ADDED PART SHORT CHECK TO PLAY THE ERROR SOUND. VERIFY SPEED
                            if( _SOModuleClient.isPartShort(partFpo, qty, 4, soItem)){
                                actionScannerError();
                            }

                            //REMOVED THE LOCATION GROUP LOOKUP COMBO AND HARD CODED IN MAIN WAREHOUSE
                            switch (_SOModuleClient.getLogicSalesClient().checkAvailable(partFpo, partQty, _SOModuleClient.so, 4, soItem, soItem.getSalesOrderItemFpo().getUomId(), eveManager, _SOModuleClient, dlgInvShort)) {
                                case 4: {
                                    if (!_SOModuleClient.showAddSOItemWizard(soItem, qty, true, true)) {
                                        return null;
                                    }
                                    finishedAdding = true;
                                    break;
                                }
                                case 2: {
                                    SODropShipChecker.checkVendorRelationShip(eveManager, soItem.getPartID());
                                    soItem.setType(SOItemTypeConst.DROP_SHIP);
                                    try {
                                        _SOModuleClient.so.addItem(soItem);
                                    } catch (FbiException e) {
                                        UtilGui.showMessageDialog(e.getMessage(), "SO Item error", 0);
                                        return null;
                                    }
                                    finishedAdding = true;
                                    break;
                                }
                                case 1: {
                                    try {
                                        _SOModuleClient.so.addItem(soItem);
                                    } catch (FbiException e) {
                                        UtilGui.showMessageDialog(e.getMessage(), "SO Item error", 0);
                                        return null;
                                    }
                                    finishedAdding = true;
                                    break;
                                }
                                case 3: {
                                    //substitute menu. HIDE ON THE SCANNER MENU
//                                    final int prodID = LogicSalesClient.getAlternativeProductID(new AlternateProductSearch(_SOModuleClient.baseFrame, eveManager, soItem.getSalesOrderItemFPO().getProductId()));
//                                    if (prodID > 0) {
//                                        _SOModuleClient.pnlQAProduct.setSelectedID(prodID);
//                                        final SOItem tempSoItem = this.addProductToSO(prodID, this.quantityFormattedTextField.getQuantity(), null, true);
//                                        if (tempSoItem != null) {
//                                            finishedAdding = true;
//                                        }
//                                        break;
//                                    }
                                    UtilGui.showMessageDialog("No Substitute Available");
                                    break;
                                }
                                case 5: {
                                    try {
                                        _SOModuleClient.so.addItem(soItem);
                                    } catch (FbiException e2) {
                                        UtilGui.showMessageDialog(e2.getMessage(), "SO Item error", 0);
                                        return null;
                                    }
                                    qty = soItem.getQuantity();
                                    finishedAdding = true;
                                    break;
                                }
                                case 0: {
                                    return null;
                                }
                                default: {
                                    addCorePart = false;
                                    finishedAdding = true;
                                    break;
                                }
                            }
                        } else {
                            try {
                                _SOModuleClient.so.addItem(soItem);
                            } catch (FbiException e3) {
                                UtilGui.showMessageDialog(e3.getMessage(), "SO Item error", 0);
                                return null;
                            }
                            finishedAdding = true;
                        }
                        soItem.setQuantity(qty, price == null);
                        if (soItem.getPartFpo().getTypeId() != PartTypeConst.SHIPPING.getId()) {
                            continue;
                        }
                        soItem.setType(SOItemTypeConst.SHIPPING);
                    }
                }
                _SOModuleClient.getLogicSalesClient().checkPrice(soItem);
                if (SystemPropertyConst.SO_CONFIRM_ZERO_MONEY.getBoolean() && productFPO.getPrice().isZero()) {
                    UtilGui.showMessageDialog("Current Price of this Product is Zero.\nPlease make corrections if necessary.", "Price is Zero", 2);
                }
                if (!Util.isEmpty(productFPO.getAlertNote())) {
                    actionScannerError();
                    UtilGui.showMessageDialog(productFPO.getAlertNote(), "Product Info", 2);
                }
                if (addCorePart) {
                    final String coreMask = SystemPropertyConst.PRODUCT_CORE_MASK.getString();
                    if (coreMask != null && coreMask.length() > 0) {
                        final EVEvent event = eveManager.createRequest("Product", "getCoreProducts");
                        event.add((Object) "Product Number", soItem.getSalesOrderItemFpo().getProductNum() + coreMask);
                        final EVEvent response = eveManager.sendAndWait(event);
                        if (response.getMessageType() == 201) {
                            UtilGui.showErrorMessageDialog("Could not add core products.", "SO Item Error");
                        } else {
                            final List<ProductFpo> coreList = (List<ProductFpo>) response.getObject((Object) "coreProduct");
                            for (final ProductFpo coreFPO : coreList) {
                                this.addProductToSO(coreFPO.getId(), qty, null, true);
                            }
                        }
                    }
                }

                _SOModuleClient.getController().setModified(true);

                _SOModuleClient.displayOnLineDisplay(soItem);
            } catch (IllegalArgumentException e4) {
                this.LOGGER.log((Priority) Level.ERROR, (Object) e4.getMessage(), (Throwable) e4);
                final OptionMessage msg = new OptionMessage(e4.getMessage());
                UtilGui.showErrorMessageDialog(e4.getMessage(), "SO Item Error");
                //_SOModuleClient.quantityFormattedTextField.requestFocus();
                return null;
            } catch (Exception e5) {
                this.LOGGER.log((Priority) Level.ERROR, (Object) e5.getMessage(), (Throwable) e5);
                //this.quantityFormattedTextField.requestFocus();
                return null;
            }
            _SOModuleClient.populateItemsTable();
            //_SOModuleClient.pnlQAProduct.setSelectedID(-1);
        }
        if (changeQty) {
            //RESET THE QTY TEXT BOX BUT IT'S PRIVATE ACCESS, SO SKIP FOR NOW
            //_SOModuleClient.quantityFormattedTextField.setQuantity(Quantity.ONE);
        }
        //this.quantityFormattedTextField.requestFocus();
        return soItem;
    }

    private void mapCustomFields(final int productId, final SOItem soItem) {
        final EVEvent request = eveManager.createRequest(MethodConst.GET_CUSTOM_FIELD_VALUES);
        request.addObject((Object) KeyConst.TABLE_ID, (Serializable) RecordTypeConst.PRODUCT);
        request.add((Object)KeyConst.RecordID, productId);
        final EVEvent response = eveManager.sendAndWait(request);
        final List<CustomField> productCustomFieldList = (List<CustomField>)response.getList((Object)KeyConst.CUSTOM_FIELDS, (Class)CustomField.class);
        CustomFieldModule.mapCustomFields((List)productCustomFieldList, soItem.getCustomFieldList());
    }





/////OLD STUFF FROM OTHER BUTTONS

    private int getSOItemCount(int selectedSOID) {
        StringBuilder query = new StringBuilder("Select count(id) as ItemCount ");
        query.append("from soitem ");
        query.append("where soid = ").append(selectedSOID).append(") ");
        query.append("AND Soitem.typeid not in (40,60,90)");
        List stateRows = this.runQuery(query.toString());
        if (!Util.isEmpty((List)stateRows)) {
            return ((QueryRow)stateRows.get(0)).getInt("ItemCount");
        }
        return -1;
    }

    private String getSONum(int SOID) {
        StringBuilder query = new StringBuilder("Select num from SO where id = ").append(SOID);
        List stateRows = this.runQuery(query.toString());
        if (!Util.isEmpty((List)stateRows)) {
            return ((QueryRow)stateRows.get(0)).getString("num");
        }
        return null;
    }

    private Boolean isIssued(int SOID) {
        StringBuilder query = new StringBuilder("Select statusid from SO where id = ").append(SOID);
        List stateRows = this.runQuery(query.toString());
        if (!Util.isEmpty((List)stateRows)) {
            if (((QueryRow)stateRows.get(0)).getInt("statusid") == 20 || ((QueryRow)stateRows.get(0)).getInt("statusid") == 25)
                return true;
            else
            {return false;}
        }
        return null;
    }

    private int getSOPickCount (int SOID) {
        StringBuilder query = new StringBuilder("Select pick.num as PickNum ");
        query.append("from so ");
        query.append("JOIN pickitem ON pickitem.orderId = so.id ");
        query.append("JOIN pick ON pick.id = pickitem.pickId ");
        query.append("Where so.id = ").append(SOID);
        query.append("GROUP BY pick.id ");
        List stateRows = this.runQuery(query.toString());
        if (!Util.isEmpty((List) stateRows)) {
            return (stateRows.size());
        }

        return -1;

    }
}
