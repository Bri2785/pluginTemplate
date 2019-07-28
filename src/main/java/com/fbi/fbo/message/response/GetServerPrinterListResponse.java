package com.fbi.fbo.message.response;


import java.util.ArrayList;

public interface GetServerPrinterListResponse extends ResponseBase {

    public static final String BEAN_NAME = "GetServerPrinterListResponse";

    ArrayList<String> getPrinters();

    void setPrinters(ArrayList<String> printers);

    boolean hasPrinters();

    String getPrinter(int i);

    int getPrinterSize();
}
