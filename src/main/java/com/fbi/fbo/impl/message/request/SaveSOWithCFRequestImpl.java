package com.fbi.fbo.impl.message.request;

import com.fbi.fbo.message.request.SaveSOWithCFRequest;
import com.fbi.fbo.orders.SalesOrder;

public class SaveSOWithCFRequestImpl extends RequestBaseImpl implements SaveSOWithCFRequest {

    private SalesOrder so;
    private boolean issue;
    private boolean ignoreItems;

    @Override
    public void setSO(final SalesOrder so) {
        this.so = so;
    }

    @Override
    public SalesOrder getSO() {
        return this.so;
    }

    public boolean getIssueFlag() {
        return issue;
    }

    @Override
    public void setIssue(boolean issue) {
        this.issue = issue;
    }
    public String getRequestName() {
        return "SaveSOWithCFRequest";
    }

    public String getResponseName() {
        return "SaveSOWithCFResponse";
    }

    @Override
    public boolean isIgnoreItems() {
        return ignoreItems;
    }

    @Override
    public void setIgnoreItems(boolean ignoreItems) {
        this.ignoreItems = ignoreItems;
    }
}
