package com.fbi.fbo.message.response;

import com.fbi.fbo.orders.SalesOrder;

public interface SaveSOWithCFResponse extends ResponseBase {

    public static final String BEAN_NAME = "SaveSOWithCFResponse";

    void setSO(final SalesOrder p0);

    SalesOrder getSO();
}
