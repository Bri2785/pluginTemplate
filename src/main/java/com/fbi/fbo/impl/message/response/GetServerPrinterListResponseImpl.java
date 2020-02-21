package com.fbi.fbo.impl.message.response;

import com.fbi.fbo.message.response.GetServerPrinterListResponse;
import com.printnode.api.impl.Printer;

import java.util.ArrayList;

public class GetServerPrinterListResponseImpl extends ResponseBaseImpl implements GetServerPrinterListResponse {

    private ArrayList<Printer> printers = new ArrayList<>();

    public GetServerPrinterListResponseImpl(){
    }

    @Override
    public ArrayList<Printer> getPrinters() {
        return printers;
    }

    @Override
    public void setPrinters(ArrayList<Printer> printers) {
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
    public Printer getPrinter(int i) {
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
