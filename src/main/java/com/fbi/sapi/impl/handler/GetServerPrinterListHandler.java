package com.fbi.sapi.impl.handler;

import com.fbi.fbdata.plugins.PluginPropertyFpo;
import com.fbi.fbo.impl.message.response.GetServerPrinterListResponseImpl;
import com.fbi.fbo.impl.message.response.MasterResponseImpl;
import com.fbi.fbo.message.Response;
import com.fbi.fbo.message.response.GetServerPrinterListResponse;
import com.fbi.util.FbiException;
import com.fbi.util.logging.FBLogger;
import com.printnode.api.impl.APIClient;
import com.printnode.api.impl.Auth;
import com.printnode.api.impl.Printer;
import com.unigrative.plugins.apiExtension.ApiExtensionsPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.print.PrintServiceLookup;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@Service("GetServerPrinterListRq")
public class GetServerPrinterListHandler extends Handler {

    private static final Logger LOGGER = LoggerFactory.getLogger((Class) GetServerPrinterListHandler.class);

    @Override
    public void execute(String s, int i, Response response) {

        MasterResponseImpl masterResponse = (MasterResponseImpl) response;

        final GetServerPrinterListResponse printerListResponse = new GetServerPrinterListResponseImpl();

        masterResponse.getResponseList().add(printerListResponse);

        //get API key
        try {

        PluginPropertyFpo propertyFpo = this.getPluginPropertyRepository().findByPluginAndKey(ApiExtensionsPlugin.MODULE_NAME, "PrintNodeApiKey");
        String apiKey = propertyFpo.getInfo();

        if (apiKey == null || apiKey.isEmpty()){
            throw new FbiException("PrintNode Api key need to be setup in module settings");
        }
        Auth auth = new Auth();
        auth.setApiKey(apiKey);

        APIClient client = new APIClient(auth);


            Printer[] printers = client.getPrinters("");

            if (printers.length == 0){
               throw new FbiException("No printers found under account: " + client.getWhoami().getEmail());
            }

            ArrayList<Printer> printerList = new ArrayList<>();
            Collections.addAll(printerList, printers);
            printerListResponse.setPrinters(printerList);

         }
         catch (FbiException var8) {
             FBLogger.error(var8.getMessage(), var8);
             printerListResponse.setStatusCode(var8.getStatusCode());
             printerListResponse.setStatusMessage(var8.getMessage());

         }
         catch (IOException ex){
             FBLogger.error(ex.getMessage(), ex);
            printerListResponse.setStatusCode(9000);
            printerListResponse.setStatusMessage(ex.getMessage());
         }


//
//
//
//
//
//        ArrayList<String> printerList = new ArrayList();
//
//        try {
//            refreshSystemPrinterList();
//
//            //locate the printer on the machine
//            final PrintService[] lookupPrintServices = PrintServiceLookup.lookupPrintServices(null, null);
//            for (final PrintService service : lookupPrintServices) {
//                printerList.add(service.getName());
//            }
//
//            printerListResponse.setPrinters(printerList);
//
//
//
//        } catch (ExceptionMainFree e2) {
//            FBLogger.error(e2.getMessage(), e2);
//            printerListResponse.setStatusCode(e2.getCode());
//            printerListResponse.setStatusMessage(e2.getMsgErr());
//        }

    }

    /**
     * Printer list does not necessarily refresh if you change the list of
     * printers within the O/S; you can run this to refresh if necessary.
     */
    public static void refreshSystemPrinterList() {

        Class[] classes = PrintServiceLookup.class.getDeclaredClasses();

        for (int i = 0; i < classes.length; i++) {

            if ("javax.print.PrintServiceLookup$Services".equals(classes[i].getName())) {

                sun.awt.AppContext.getAppContext().remove(classes[i]);
                break;
            }
        }
    }
}
