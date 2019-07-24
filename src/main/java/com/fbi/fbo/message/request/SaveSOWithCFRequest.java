package com.fbi.fbo.message.request;

import com.fbi.fbo.orders.SalesOrder;

public interface SaveSOWithCFRequest extends RequestBase {

    public static final String BEAN_NAME = "SaveSOWithCFRequest";

    void setSO(final SalesOrder p0);

    SalesOrder getSO();

    boolean getIssueFlag();

    void setIssue(final boolean p0);

    boolean isIgnoreItems();

    void setIgnoreItems(final boolean p0);
}
