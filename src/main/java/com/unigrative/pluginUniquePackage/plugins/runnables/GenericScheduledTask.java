package com.unigrative.pluginUniquePackage.plugins.runnables;

import com.fbi.plugins.ScheduledFishbowlPlugin;
import com.unigrative.pluginUniquePackage.plugins.GenericPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GenericScheduledTask extends ScheduledFishbowlPlugin {

    //IF THE CLASS NAME OR PATH CHANGES, THE SERVER WILL NEED TO BE RESTARTED SO THE CLASS CAN BE SCANNED AND FOUND
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericScheduledTask.class);

    public GenericScheduledTask() {
        this.setModuleName(GenericPlugin.MODULE_NAME);
    }

    @Override
    public void run(HashMap args) throws Exception {
        LOGGER.debug("Running task");

        Map params = new HashMap<String, String>(); //to save results and last sync time

        StringBuilder taskResult = new StringBuilder();

        //String lastSyncDate = this.getPluginData(CertProperty.LAST_SYNC_TIMEKey);

        //taskResult.append(String.format("Last Sync Time: %s", lastSyncDate)).append(System.lineSeparator());

        //params.put(CertProperty.LAST_TASK_RESULTKey, taskResult.toString());
        this.savePluginData(params);

        LOGGER.debug("Scheduled Task has run");
    }
}
