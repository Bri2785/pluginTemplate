package com.evnt.client.modules.so;

import com.evnt.client.modules.customer.CustomerModuleClient;
import com.fbi.gui.util.UtilGui;
import com.fbi.plugins.FishbowlPluginButton;
import com.unigrative.pluginUniquePackage.plugins.GenericPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class ModuleButton extends FishbowlPluginButton {

    private static final Logger LOGGER = LoggerFactory.getLogger((Class) ModuleButton.class);

    private SOModuleClient _SOModuleClient;
    private CustomerModuleClient _CustomerModule;

    public ModuleButton() {

        this.setModuleName("Customer"); //CHANGE
        this.setPluginName(GenericPlugin.MODULE_FRIENDLY_NAME);
        this.setIcon(new ImageIcon(this.getClass().getResource("/icon24/textanddocuments/documents/document_new.png")));
        this.setText("Create Order"); //CHANGE


        //button action
        this.addActionListener((event) -> {
            this.createOrder();
        });

        //Access to client modules
        _SOModuleClient = (SOModuleClient) GenericPlugin.getInstance().getModule("Sales Order");
        _CustomerModule = (CustomerModuleClient) GenericPlugin.getInstance().getModule("Customer");
    }

    private void createOrder() {
        if (_CustomerModule != null && _CustomerModule.getObjectId() > 0) {

            GenericPlugin.getInstance().showModule("Sales Order");
            if (_SOModuleClient.checkForSave()) {
                _SOModuleClient.createSO();
                _SOModuleClient.getSO().setCustomerId(_CustomerModule.getObjectId(), true);
                _SOModuleClient.setData();
            }
        }
        else{
            UtilGui.showMessageDialog("Load a customer first");
        }
    }
}
