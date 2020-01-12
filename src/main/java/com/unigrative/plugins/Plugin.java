package com.unigrative.plugins;

import com.evnt.client.common.EVEManager;
import com.evnt.client.common.EVEManagerUtil;
import com.fbi.fbo.impl.ApiCallType;
import com.fbi.fbo.impl.dataexport.QueryRow;
import com.fbi.fbo.message.request.RequestBase;
import com.fbi.fbo.message.response.ResponseBase;
import com.fbi.gui.button.FBMainToolbarButton;
import com.fbi.gui.panel.TitlePanel;
import com.fbi.plugins.FishbowlPlugin;
import com.fbi.sdk.constants.MenuGroupNameConst;
import com.unigrative.plugins.exception.FishbowlException;
import com.unigrative.plugins.fbapi.ApiCaller;
import com.unigrative.plugins.panels.SettingsPanel;
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

public class Plugin extends FishbowlPlugin implements PropertyGetter, Repository.RunSql, ApiCaller {

    private static final String MODULE_NAME = "TestPlugin"; //CHANGE
    private static final String MODULE_FRIENDLY_NAME = "Plugin Addons"; //CHANGE
    private static final Logger LOGGER = LoggerFactory.getLogger((Class)Plugin.class);

    private static final String PLUGIN_GENERIC_PANEL = "pluginGenericPanel";

    private Repository repository;

    private static Plugin instance = null;

    EVEManager eveManager = EVEManagerUtil.getEveManager(); //get access to eve manager

    private TitlePanel titleLabel;
    private JToolBar mainToolBar;
    private FBMainToolbarButton btnSave;

    private JPanel pnlCards;
    //private JPanel pnlSettings;
    private SettingsPanel settingsPanel;

    public Plugin() {
        instance = this; //this is so we can access the FishbowlPlugin module methods from other classes
        this.setModuleName(Plugin.MODULE_NAME);
        this.setMenuGroup(MenuGroupNameConst.INTEGRATIONS);//this is the module group it shows up in
        this.setMenuListLocation(1000); //bottom of the list
        this.setIconClassPath("/images/unigrative48.png"); // make sure there is a 24 version in the folder so it can use that for the tabs
        this.setDefaultHelpPath("https://www.unigrative.com/kbtopic/fishbowl-plugins/");

        //this.addAccessRight("Import Button"); //optional access rights that can be set and checked later

        this.repository = new Repository(this);

    }


    public static Plugin getInstance() {
        return instance;
    }

    public String getModuleTitle() {
        return "<html><center>TEST<br>PLUGIN</center></html>"; //CHANGE -> THIS SHOWS IN THE MODULE LIST
    }

    public String getProperty(final String key) {
        return this.repository.getProperty(key);
    }

    public List<QueryRow> executeSql(final String query) {
        return (List<QueryRow>)this.runQuery(query);
    }


    private void loadSettings() {
        LOGGER.info("Loading Settings");
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

        LOGGER.info("Settings Loaded");

    }

    protected void saveSettings(){
        LOGGER.info("Saving settings");

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

        LOGGER.info("Settings Saved");
    }

    private void btnSaveActionPerformed() {
        this.saveSettings();

    }

    protected void initModule() {
        super.initModule();
        this.initComponents(); //HAS TO COME FIRST SINCE THE PANELS NEED TO BE MADE
        this.setMainToolBar(this.mainToolBar);
        this.initLayout(); //FILLS THE CARD PANEL WITH THE INTERIOR PANELS
        this.setButtonPrintVisible(false); //OPTIONAL
        this.setButtonEmailVisible(false); //OPTIONAL


    }

    private void initLayout() {
        //PANELS TO BE ADDED TO THE CARD LAYOUT (TYPICALLY ONLY ONE UNLESS THE ENTIRE SCREEN NEEDS TO BE SWITCHED)
        //TABBED PANELS ARE CREATED SEPARATELY (GENERIC PANEL)
        this.settingsPanel = new SettingsPanel(this);
        this.pnlCards.add(this.settingsPanel, "SettingsPanel" ); //CHANGE STRING NAME IF DESIRED
        this.hideShowPanels(); //Makes the interior panel visible
    }

    private void hideShowPanels() {
        final CardLayout layout = (CardLayout)this.pnlCards.getLayout();
        this.enableControls(true);
        layout.show(this.pnlCards, PLUGIN_GENERIC_PANEL);
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

            //GENERIC PANEL
            //this.pnlSettings = new JPanel();

            this.mainToolBar.setFloatable(false);
            this.mainToolBar.setRollover(true);
            this.mainToolBar.setName("mainToolBar");

            this.btnSave.setIcon((Icon) new ImageIcon(this.getClass().getResource("/icon24/filesystem/disks/disk_gold.png")));
            this.btnSave.setText("Save");
            this.btnSave.setToolTipText("Save your Plugin settings."); //CHANGE NAME
            this.btnSave.setHorizontalTextPosition(0);
            this.btnSave.setIconTextGap(0);
            this.btnSave.setMargin(new Insets(0, 2, 0, 2));
            this.btnSave.setName("btnSave");
            this.btnSave.setVerticalTextPosition(3);
            this.btnSave.addActionListener((ActionListener) new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    Plugin.this.btnSaveActionPerformed();
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
