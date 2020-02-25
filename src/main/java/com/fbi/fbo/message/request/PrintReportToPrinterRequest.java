package com.fbi.fbo.message.request;

import com.fbi.fbo.property.ReportParam;

import java.util.List;

public interface PrintReportToPrinterRequest extends RequestBase{

    public static final String BEAN_NAME = "PrintReportToPrinterRequest";

    int getReportId();

    void setReportId(int reportId);

    void setParamList(final List<ReportParam> p0);

    List<ReportParam> getParamList();

    void addReportParam(final ReportParam p0);

    boolean hasParams();

    int getParamListSize();

    ReportParam getReportParam(final int p0);

    int getPrinterId();

    void setPrinterId(int printerId);

    int getNumberOfCopies();

    void setNumberOfCopies(final int numberOfCopies);
}
