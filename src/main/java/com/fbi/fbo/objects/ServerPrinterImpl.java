package com.fbi.fbo.objects;

public class ServerPrinterImpl {

    public String getPrinterName() {
        return PrinterName;
    }

    public void setPrinterName(String printerName) {
        PrinterName = printerName;
    }

    private String PrinterName;
}
