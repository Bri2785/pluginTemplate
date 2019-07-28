package com.fbi.fbo.impl;

import com.fbi.fbo.impl.message.request.GetPartRequestImpl;
import com.fbi.fbo.impl.message.request.GetServerPrinterListRequestImpl;
import com.fbi.fbo.impl.message.request.PrintReportToPrinterRequestImpl;
import com.fbi.fbo.impl.message.request.SaveSOWithCFRequestImpl;
import com.fbi.fbo.impl.message.response.GetPartResponseImpl;
import com.fbi.fbo.impl.message.response.GetServerPrinterListResponseImpl;
import com.fbi.fbo.impl.message.response.PrintReportToPrinterResponseImpl;
import com.fbi.fbo.impl.message.response.SaveSOWithCFResponseImpl;
import com.fbi.fbo.message.request.RequestBase;
import com.fbi.fbo.message.response.ResponseBase;

public enum ApiCallType {
    GET_PART("GetPart", GetPartRequestImpl.class, GetPartResponseImpl.class),
    SAVE_SO_WITH_CF("SaveSOWithCF", SaveSOWithCFRequestImpl.class, SaveSOWithCFResponseImpl.class),
    PRINT_REPORT_TO_PRINTER("PrintReportToPrinter", PrintReportToPrinterRequestImpl.class, PrintReportToPrinterResponseImpl.class),
    GET_SERVER_PRINTER_LIST("GetServerPrinterList", GetServerPrinterListRequestImpl.class, GetServerPrinterListResponseImpl.class);



    private String requestName;
    private Class<? extends RequestBase> requestClass;
    private Class<? extends ResponseBase> responseClass;

    private ApiCallType(final String requestName, final Class<? extends RequestBase> requestClass, final Class<? extends ResponseBase> responseClass) {
        this.requestName = requestName;
        this.requestClass = requestClass;
        this.responseClass = responseClass;
    }

    public String getRequestName() {
        return this.requestName + "Rq";
    }

    public String getResponseName() {
        return this.requestName + "Rs";
    }

    public Class<? extends RequestBase> getRequestClass() {
        return this.requestClass;
    }

    public Class<? extends ResponseBase> getResponseClass() {
        return this.responseClass;
    }

    public static ApiCallType getRequestType(final String name) {
        for (final ApiCallType callType : values()) {
            if (callType.getRequestName().equals(name)) {
                return callType;
            }
        }
        return null;
    }

    public static ApiCallType getResponseType(final String name) {
        for (final ApiCallType callType : values()) {
            if (callType.getResponseName().equals(name)) {
                return callType;
            }
        }
        return null;
    }
}

