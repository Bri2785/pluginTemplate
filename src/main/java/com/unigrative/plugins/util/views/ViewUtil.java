package com.unigrative.plugins.util.views;

import com.evnt.client.common.EVEManager;
import com.evnt.common.MethodConst;
import com.evnt.eve.event.EVEvent;
import com.evnt.util.KeyConst;
import com.fbi.fbdata.general.DataExportFpo;
import com.fbi.fbo.impl.dataexport.DataExportResult;
import com.fbi.gui.util.UtilGui;
import com.fbi.util.exception.ExceptionMainFree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class)ViewUtil.class);

    public static void createView(String SQLStatement, EVEManager eveManager ){

        RunQuery(SQLStatement.toString(), eveManager);
    }

    private static void RunQuery(String query, EVEManager eveManager) {
        LOGGER.debug("Running Query");
        LOGGER.debug(query);

        EVEvent request = eveManager.createRequest(MethodConst.RUN_DATA_EXPORT);
        request.add(KeyConst.DATA_EXPORT_QUERY, query);
        request.add(KeyConst.SUPPORT_LOGGED_IN, true);

        try {
            DataExportFpo.validateSQL(query, true);
        } catch (ExceptionMainFree var4) {
            LOGGER.error("Error validating query", var4);
        }

        EVEvent response = eveManager.sendAndWait(request);
        DataExportResult queryData;
        queryData = (DataExportResult)response.getObject(KeyConst.DATA_EXPORT_RESULTS, DataExportResult.class);

        if (response.isMessageException()){
            UtilGui.showMessageDialog("Error Running Query. Check logs for details");
        }
    }

    private static void grantViewAccess(String viewName, EVEManager eveManager){
        LOGGER.debug("Running grant on " + viewName);

        String query = "Grant SELECT ON " + viewName + " TO 'gone'";

        EVEvent request = eveManager.createRequest(MethodConst.RUN_DATA_EXPORT);
        request.add(KeyConst.DATA_EXPORT_QUERY, query);
        request.add(KeyConst.SUPPORT_LOGGED_IN, true);

        try {
            DataExportFpo.validateSQL(query, true);
        } catch (ExceptionMainFree var4) {
            LOGGER.error("Error validating query", var4);
        }

        EVEvent response = eveManager.sendAndWait(request);
        DataExportResult queryData;
        queryData = (DataExportResult)response.getObject(KeyConst.DATA_EXPORT_RESULTS, DataExportResult.class);

        if (response.isMessageException()){
            UtilGui.showMessageDialog("Error granting select permissions. Check logs for details");
        }
    }
}
