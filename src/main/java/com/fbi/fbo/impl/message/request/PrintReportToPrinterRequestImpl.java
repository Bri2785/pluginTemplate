package com.fbi.fbo.impl.message.request;

import com.evnt.util.Util;
import com.fbi.fbo.message.request.PrintReportToPrinterRequest;
import com.fbi.fbo.property.ReportParam;

import java.util.ArrayList;
import java.util.List;

public class PrintReportToPrinterRequestImpl extends RequestBaseImpl implements PrintReportToPrinterRequest {

    private String _reportName;
    private List<ReportParam> paramList;
    private int numberOfCopies;
    private String printerName;


    @Override
    public String getReportName() {
        return this._reportName;
    }

    @Override
    public void setReportName(String reportName) {
        this._reportName = reportName;
    }

    @Override
    public void setParamList(List<ReportParam> p0) {
        this.paramList = p0;
    }

    @Override
    public List<ReportParam> getParamList() {
        return this.paramList;
    }

    private void checkParamList() {
        if (this.paramList == null) {
            this.paramList = new ArrayList<ReportParam>();
        }
    }

    @Override
    public void addReportParam(final ReportParam reportParam) {
        this.checkParamList();
        this.paramList.add(reportParam);
    }

    @Override
    public int getParamListSize() {
        this.checkParamList();
        return this.paramList.size();
    }

    @Override
    public ReportParam getReportParam(final int index) {
        if (index < this.getParamListSize() && index >= 0) {
            return this.paramList.get(index);
        }
        return null;
    }

    @Override
    public boolean hasParams() {
        return !Util.isEmpty((List)this.paramList);
    }

    @Override
    public String getPrinterName() {
        return printerName;
    }

    @Override
    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    @Override
    public int getNumberOfCopies() {
        return this.numberOfCopies;
    }

    @Override
    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    @Override
    public String getRequestName() {
        return "PrintReportToPrinterRequest";
    }

    @Override
    public String getResponseName() {
        return "PrintReportToPrinterResponse";
    }
}
