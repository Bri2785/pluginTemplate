package com.fbi.sapi.impl.handler;

import com.fbi.fbo.impl.message.response.GetServerPrinterListResponseImpl;
import com.fbi.fbo.impl.message.response.MasterResponseImpl;
import com.fbi.fbo.message.Response;
import com.fbi.fbo.message.response.GetServerPrinterListResponse;
import com.fbi.util.FbiException;
import com.fbi.util.exception.ExceptionMainFree;
import com.fbi.util.logging.FBLogger;
import org.springframework.stereotype.Service;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.util.ArrayList;
import java.util.List;

@Service("GetServerPrinterListRq")
public class GetServerPrinterListHandler extends Handler {


    @Override
    public void execute(String s, int i, Response response) {

        MasterResponseImpl masterResponse = (MasterResponseImpl) response;

        final GetServerPrinterListResponse printerListResponse = new GetServerPrinterListResponseImpl();

        masterResponse.getResponseList().add(printerListResponse);

        ArrayList<String> printerList = new ArrayList();

        try {
            refreshSystemPrinterList();

            //locate the printer on the machine
            final PrintService[] lookupPrintServices = PrintServiceLookup.lookupPrintServices(null, null);
            for (final PrintService service : lookupPrintServices) {
                printerList.add(service.getName());
            }

            printerListResponse.setPrinters(printerList);



        } catch (ExceptionMainFree e2) {
            FBLogger.error(e2.getMessage(), e2);
            printerListResponse.setStatusCode(e2.getCode());
            printerListResponse.setStatusMessage(e2.getMsgErr());
        }

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
