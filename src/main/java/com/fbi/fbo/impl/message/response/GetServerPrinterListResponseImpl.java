package com.fbi.fbo.impl.message.response;

import com.fbi.fbo.message.response.GetServerPrinterListResponse;

import java.util.ArrayList;

public class GetServerPrinterListResponseImpl extends ResponseBaseImpl implements GetServerPrinterListResponse {

    private ArrayList<String> printers = new ArrayList<>();

    public GetServerPrinterListResponseImpl(){
    }

    @Override
    public ArrayList<String> getPrinters() {
        return printers;
    }

    @Override
    public void setPrinters(ArrayList<String> printers) {
        this.printers = printers;
    }

    @Override
    public boolean hasPrinters() {
        if (printers != null && printers.size() > 0){
            return true;
        }
        return false;
    }

    @Override
    public String getPrinter(int i) {
        if (printers != null && printers.size() > 0){
            return printers.get(i);
        }
        return null;
    }

    @Override
    public int getPrinterSize() {
        if (printers != null){
            return printers.size();
        }
        return 0;
    }
}
