package com.fbi.fbo.impl.message.response;

import com.fbi.fbo.message.response.SaveSOWithCFResponse;
import com.fbi.fbo.orders.SalesOrder;

public class SaveSOWithCFResponseImpl extends ResponseBaseImpl implements SaveSOWithCFResponse {

    private SalesOrder so;

    @Override
    public void setSO(SalesOrder p0) {
        this.so = p0;
    }

    @Override
    public SalesOrder getSO() {
        return this.so;
    }
}
