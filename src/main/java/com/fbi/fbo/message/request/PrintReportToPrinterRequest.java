package com.fbi.fbo.message.request;

import com.fbi.fbo.property.ReportParam;

import java.util.List;

public interface PrintReportToPrinterRequest extends RequestBase{

    public static final String BEAN_NAME = "PrintReportToPrinterRequest";

    String getReportName();

    void setReportName(final String reportName);

    void setParamList(final List<ReportParam> p0);

    List<ReportParam> getParamList();

    void addReportParam(final ReportParam p0);

    boolean hasParams();

    int getParamListSize();

    ReportParam getReportParam(final int p0);

    String getPrinterName();

    void setPrinterName(String printerName);

    int getNumberOfCopies();

    void setNumberOfCopies(final int numberOfCopies);
}
