package com.unigrative.plugins.apiExtension;

import com.evnt.client.common.EVEManager;
import com.evnt.client.common.EVEManagerUtil;
import com.fbi.fbo.impl.dataexport.QueryRow;
import com.fbi.gui.button.FBMainToolbarButton;
import com.fbi.gui.misc.GUIProperties;
import com.fbi.gui.panel.TitlePanel;
import com.fbi.plugins.FishbowlPlugin;
import com.fbi.sdk.constants.MenuGroupNameConst;
import com.unigrative.plugins.apiExtension.panels.SettingsPanel;
import com.unigrative.plugins.apiExtension.repository.Repository;
import com.unigrative.plugins.apiExtension.util.property.PropertyGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiExtensionsPlugin extends FishbowlPlugin implements PropertyGetter, Repository.RunSql {
    public static final String MODULE_NAME = "ApiExtensions"; //CHANGE
    public static final String MODULE_FRIENDLY_NAME = "Api Extensions"; //CHANGE
    private static final Logger LOGGER = LoggerFactory.getLogger((Class)ApiExtensionsPlugin.class);

    private static final String PLUGIN_GENERIC_PANEL = "pluginGenericPanel";

    private Repository repository;

    private static ApiExtensionsPlugin instance = null;

    private EVEManager eveManager = EVEManagerUtil.getEveManager(); //get access to eve manager

    private TitlePanel titleLabel;
    private JToolBar mainToolBar;
    private FBMainToolbarButton btnSave;

    private JPanel pnlCards;

    private SettingsPanel settingsPanel;

    public ApiExtensionsPlugin() {
        instance = this; //this is so we can access the FishbowlPlugin module methods from other classes
        this.setModuleName(ApiExtensionsPlugin.MODULE_FRIENDLY_NAME);
        this.setMenuGroup(MenuGroupNameConst.INTEGRATIONS);//this is the module group it shows up in
        this.setMenuListLocation(1000); //bottom of the list
        this.setIconClassPath("/images/unigrative48.png"); // make sure there is a 24 version in the folder so it can use that for the tabs
        this.setDefaultHelpPath("https://www.unigrative.com/kbtopic/fishbowl-plugins/");

        this.repository = new Repository(this);

    }

    protected void initModule() {
        super.initModule();
        this.initComponents(); //HAS TO COME FIRST SINCE THE PANELS NEED TO BE MADE
        this.setMainToolBar(this.mainToolBar);
        this.initLayout(); //FILLS THE CARD PANEL WITH THE INTERIOR PANELS
        this.setButtonPrintVisible(false); //TODO: OPTIONAL
        this.setButtonEmailVisible(false); //TODO : OPTIONAL

        //GUIProperties.registerComponent(this.masterDetailPanel, this.getModuleName()); //TODO HIDE IF NOT USING MASTER DETAIL. SAVES WIDTH

        GUIProperties.loadComponents(this.getModuleName());
    }

    @Override
    public boolean activateModule() {
        if (this.eveManager.isConnected()) {
            super.activateModule();
            if (this.isInitialized()) {
                this.settingsPanel.loadSettings();
                //initModels); //TODO only needed if adding custom tables
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


    public static ApiExtensionsPlugin getInstance() {
        return instance;
    }

    public String getModuleTitle() {
        return "<html><center>Api<br>Extensions</center></html>"; //CHANGE -> THIS SHOWS IN THE MODULE LIST
    }

    public String getProperty(final String key) {
        return this.repository.getProperty(key);
    }

    public List<QueryRow> executeSql(final String query) {
        return (List<QueryRow>)this.runQuery(query);
    }

    @Override
    public int getObjectId() {
        return 1; //this is usually the id for the loaded object. Set to 1 to get our own function. This calls loadData() override
    }

    @Override
    public void loadData(int objectId) {
        //custom load data method. Also called on refreshButton being clicked
        this.settingsPanel.loadSettings();
    }


    protected void saveSettings(){
        this.settingsPanel.saveSettings();
    }

    private void btnSaveActionPerformed() {
        this.saveSettings();

    }



    private void initLayout() {
        //PANELS TO BE ADDED TO THE TABBED LAYOUT IF DESIRED
        this.settingsPanel = new SettingsPanel(this);
        this.pnlCards.add(settingsPanel, PLUGIN_GENERIC_PANEL );
        this.hideShowPanels();
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
            this.pnlCards = new JPanel();
            this.mainToolBar = new JToolBar();
            this.btnSave = new FBMainToolbarButton();

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
                    ApiExtensionsPlugin.this.btnSaveActionPerformed();
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





            this.pnlCards.setName("pnlCards");
            this.pnlCards.setLayout(new CardLayout());
            this.add((Component) this.pnlCards, (Object) new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));

        }
        catch (Exception e){
            LOGGER.error("Error in init",e);
        }
        LOGGER.info("init done");
    }

}
