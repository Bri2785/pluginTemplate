package com.fbi.fbo.message.response;


import com.printnode.api.Printer;

import java.util.ArrayList;

public interface GetServerPrinterListResponse extends ResponseBase {

    public static final String BEAN_NAME = "GetServerPrinterListResponse";

    ArrayList<Printer> getPrinters();

    void setPrinters(ArrayList<Printer> printers);

    boolean hasPrinters();

    Printer getPrinter(int i);

    int getPrinterSize();
}
