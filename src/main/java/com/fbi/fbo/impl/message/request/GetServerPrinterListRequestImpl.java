package com.fbi.fbo.impl.message.request;

import com.fbi.fbo.message.request.GetServerPrinterListRequest;

public class GetServerPrinterListRequestImpl extends RequestBaseImpl implements GetServerPrinterListRequest {


    @Override
    public String getRequestName() {
        return "GetServerPrinterListRequest";
    }

    @Override
    public String getResponseName() {
        return "GetServerPrinterListResponse";
    }
}
