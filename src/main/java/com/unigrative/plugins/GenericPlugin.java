package com.unigrative.plugins;

import com.evnt.client.common.EVEManager;
import com.evnt.client.common.EVEManagerUtil;
import com.fbi.fbo.impl.ApiCallType;
import com.fbi.fbo.impl.dataexport.QueryRow;
import com.fbi.fbo.message.request.RequestBase;
import com.fbi.fbo.message.response.ResponseBase;
import com.fbi.gui.button.FBMainToolbarButton;
import com.fbi.gui.misc.GUIProperties;
import com.fbi.gui.panel.TitlePanel;
import com.fbi.plugins.FishbowlPlugin;
import com.fbi.sdk.constants.MenuGroupNameConst;
import com.unigrative.plugins.exception.FishbowlException;
import com.unigrative.plugins.fbapi.ApiCaller;
import com.unigrative.plugins.models.InitializeModels;
import com.unigrative.plugins.panels.masterdetailsearch.MasterDetailPanel;
import com.unigrative.plugins.repository.Repository;
import com.unigrative.plugins.util.property.PropertyGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericPlugin extends FishbowlPlugin implements PropertyGetter, Repository.RunSql, ApiCaller {

    private static final String MODULE_NAME = "GenericPlugin"; //TODO CHANGE
    public static final String MODULE_FRIENDLY_NAME = "Generic Plugin"; //TODO CHANGE
    private static final Logger LOGGER = LoggerFactory.getLogger((Class) GenericPlugin.class);

    private static final String PLUGIN_GENERIC_PANEL = "pluginGenericPanel";

    private Repository repository;

    private static GenericPlugin instance = null;

    EVEManager eveManager = EVEManagerUtil.getEveManager(); //get access to eve manager

    private TitlePanel titleLabel;
    private JToolBar mainToolBar;
    private FBMainToolbarButton btnSave;

    private JPanel pnlCards;

    //PICK A PANEL TO ADD TO THE CARDS PANEL UNLESS WE HAVE A WAY TO SWITCH BETWEEN THEM
    //GENERIC SETTINGS PANEL OPTION
    //private SettingsPanel settingsPanel;

    //MASTER DETAIL TABLE SEARCH OPTION
    private MasterDetailPanel masterDetailPanel;



    public GenericPlugin() {
        instance = this; //this is so we can access the FishbowlPlugin module methods from other classes
        this.setModuleName(GenericPlugin.MODULE_NAME);
        this.setMenuGroup(MenuGroupNameConst.INTEGRATIONS);//this is the module group it shows up in
        this.setMenuListLocation(1000); //bottom of the list
        this.setIconClassPath("/images/unigrative48.png"); // make sure there is a 24 version in the folder so it can use that for the tabs
        this.setDefaultHelpPath("https://www.unigrative.com/kbtopic/fishbowl-plugins/");

        //this.addAccessRight("Import Button"); //optional access rights that can be set and checked later

        this.setButtonPrintVisible(false); //CHANGE IF NEEDED
        this.setButtonEmailVisible(false);

        this.repository = new Repository(this);



    }

    protected void initModule() {
        super.initModule();
        this.initComponents(); //HAS TO COME FIRST SINCE THE PANELS NEED TO BE MADE
        this.setMainToolBar(this.mainToolBar);
        this.initLayout(); //FILLS THE CARD PANEL WITH THE INTERIOR PANELS
        this.setButtonPrintVisible(false); //TODO: OPTIONAL
        this.setButtonEmailVisible(false); //TODO : OPTIONAL

        GUIProperties.registerComponent(this.masterDetailPanel, this.getModuleName()); //TODO HIDE IF NOT USING MASTER DETAIL. SAVES WIDTH

        GUIProperties.loadComponents(this.getModuleName());
    }

    @Override
    public boolean activateModule() {
        if (this.eveManager.isConnected()) {
            super.activateModule();
            if (this.isInitialized()) {
                LOGGER.info("Initializing models");
                InitializeModels.init(this); //TODO: build tables if needed
                this.masterDetailPanel.searchPanel.executeSearch(); //TODO : ONLY NEEDED TO SEARCH RIGHT ON START UP

                return this.isInitialized();
            }
        }

        return false;
    }

    public boolean closeModule() {
        GUIProperties.saveComponents(this.getModuleName());
        //this.getController().setModified(false); //TODO: TURN ON WHEN CONTROLLER IS SETUP
        return super.closeModule();
    }
    //TODO: REGISTER CONTROLLER FOR MASTER DETAIL CHANGES TO DETAILS


    public static GenericPlugin getInstance() {
        return instance; //used from other places (buttons) to get access to the EVEManager since that's restricted to the plugin class
    }

    public String getModuleTitle() {
        return "<html><center>Generic<br>Plugin</center></html>"; // TODO CHANGE -> THIS SHOWS IN THE MODULE LIST
    }

    public EVEManager getPluginEveManager(){
        return eveManager;
    }

    public String getProperty(final String key) {
        return this.repository.getProperty(key);
    }

    public List<QueryRow> executeSql(final String query) {
        return (List<QueryRow>)this.runQuery(query);
    }


    private void loadSettings() {
        LOGGER.debug("Loading Settings");

//        this.txtSqlConnectionUrl.setText(Property.SQL_CONNECTION_URL.get(this));
//        LOGGER.info(Property.SQL_CONNECTION_URL.get(this));
//        this.txtUsername.setText(Property.USERNAME.get(this));
//        this.txtPassword.setText(Encryptor.getInstance().decrypt(Property.PASSWORD.get(this)));
//
//        this.apiKeyTextField.setText(Property.CC_API_KEY.get(this));
//        this.OAuthTokenTextField.setText(Property.CC_TOKEN.get(this));
//        this.txtLastSync.setText(Property.LAST_SYNC_TIME.get(this));
//
//        this.txtCampaignDate.setText(Property.CAMPAIGN_CREATED_DATE.get(this));

        LOGGER.debug("Settings Loaded");

    }

    protected void saveSettings(){
        LOGGER.debug("Saving settings");

        final Map<String, String> properties = new HashMap<>();

//        properties.put(Property.USERNAME.getKey(), txtUsername.getText());
//        properties.put(Property.PASSWORD.getKey(), Encryptor.getInstance().encrypt(txtPassword.getText()));
//        properties.put(Property.SQL_CONNECTION_URL.getKey(), txtSqlConnectionUrl.getText());
//
//        properties.put(Property.CC_API_KEY.getKey(), apiKeyTextField.getText());
//        properties.put(Property.CC_TOKEN.getKey(), OAuthTokenTextField.getText());
//
//
//        String lastSync = "";
//        DateTimeFormatter format = ISODateTimeFormat.dateHourMinuteSecond();
//        try {
//
//            lastSync = format.print(format.parseDateTime(txtLastSync.getText())); //set in settings
//        }
//        catch (Exception e){
//            LOGGER.error("Unable to parse Last Sync Time", e);
//            UtilGui.showMessageDialog("Unable to parse Last Sync time");
//            return; //dont save
//        }
//
//        properties.put(Property.LAST_SYNC_TIME.getKey(), lastSync);
//
//
//        String campaignDate = "";
//        try {
//            campaignDate = format.print(format.parseDateTime(txtCampaignDate.getText())); //set in settings
//
//        }
//        catch (Exception e){
//            LOGGER.error("Unable to parse campaign date", e);
//            UtilGui.showMessageDialog("Unable to parse campaign date");
//            return; //dont save
//        }
//
//        properties.put(Property.CAMPAIGN_CREATED_DATE.getKey(), campaignDate);
//


        this.savePluginProperties(properties);

        LOGGER.debug("Settings Saved");
    }

    private void btnSaveActionPerformed() {
        this.saveSettings();

    }



    private void initLayout() {
        //PANELS TO BE ADDED TO THE CARD LAYOUT (TYPICALLY ONLY ONE UNLESS THE ENTIRE SCREEN NEEDS TO BE SWITCHED)
        //TABBED PANELS ARE CREATED SEPARATELY (SETTINGS PANEL)

        //GENERIC SETTINGS PANEL
//        this.settingsPanel = new SettingsPanel(this);
//        this.pnlCards.add(this.settingsPanel, PLUGIN_GENERIC_PANEL );

        //GENERIC MASTER DETAIL TABLE
        this.masterDetailPanel = new MasterDetailPanel(this);
        this.pnlCards.add(this.masterDetailPanel, PLUGIN_GENERIC_PANEL);


        this.hideShowPanels(); //Makes the interior panel visible
    }

    private void hideShowPanels() {
        try {
            final CardLayout layout = (CardLayout) this.pnlCards.getLayout();
            this.enableControls(true);
            layout.show(this.pnlCards, PLUGIN_GENERIC_PANEL); //DIFFERENT CARD NAMES IF USING IT
        }
        catch (Exception e){
            LOGGER.error("Error showing view", e);
        }
    }

    private void enableControls(final boolean enable) {
        this.btnSave.setEnabled(enable);
    }

    private void initComponents() {
        try {
            this.titleLabel = new TitlePanel();
            this.pnlCards = new JPanel(); //Tabbed layout Option
            this.mainToolBar = new JToolBar();
            this.btnSave = new FBMainToolbarButton();

            this.mainToolBar.setFloatable(false);
            this.mainToolBar.setRollover(true);
            this.mainToolBar.setName("mainToolBar");

            this.btnSave.setIcon((Icon) new ImageIcon(this.getClass().getResource("/icon24/filesystem/disks/disk_gold.png")));
            this.btnSave.setText("Save");
            this.btnSave.setToolTipText("Save your GenericPlugin settings."); //CHANGE NAME
            this.btnSave.setHorizontalTextPosition(0);
            this.btnSave.setIconTextGap(0);
            this.btnSave.setMargin(new Insets(0, 2, 0, 2));
            this.btnSave.setName("btnSave");
            this.btnSave.setVerticalTextPosition(3);
            this.btnSave.addActionListener((ActionListener) new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    GenericPlugin.this.btnSaveActionPerformed();
                }
            });
            this.mainToolBar.add((Component) this.btnSave);

//HEADER LABEL AT THE TOP OF THE MODULE
            this.setName("this");
            this.setLayout((LayoutManager) new GridBagLayout());
            ((GridBagLayout) this.getLayout()).columnWidths = new int[]{0, 0};
            ((GridBagLayout) this.getLayout()).rowHeights = new int[]{0, 0, 0};
            ((GridBagLayout) this.getLayout()).columnWeights = new double[]{1.0, 1.0E-4};
            ((GridBagLayout) this.getLayout()).rowWeights = new double[]{0.0, 1.0, 1.0E-4};

            this.titleLabel.setModuleIcon(new ImageIcon(this.getClass().getResource("/images/unigrative32.png"))); //CHANGE
            this.titleLabel.setModuleTitle(MODULE_FRIENDLY_NAME);
            this.titleLabel.setBackground(new Color(44, 94, 140));
            this.titleLabel.setName("titleLabel");
            this.add((Component) this.titleLabel, (Object) new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));




//Layout container for module
            this.pnlCards.setName("pnlCards");
            this.pnlCards.setLayout(new CardLayout());
            this.add((Component) this.pnlCards, (Object) new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));

        }
        catch (Exception e){
            LOGGER.error("Error in init",e);
        }
        LOGGER.info("init done");
    }

    @Override
    public ResponseBase call(ApiCallType requestType, RequestBase requestBase) throws FishbowlException {
        try {
            return this.runApiRequest(requestType, requestBase);
        }
        catch (Exception e) {
            throw new FishbowlException(e);
        }
    }
}
