package com.fbi.fbo.impl.message.request;

import com.fbi.fbo.message.request.VoidPickRequest;

public class VoidPickRequestImpl extends RequestBaseImpl implements VoidPickRequest {

    private int pickId;


    @Override
    public int getPickId() {
        return pickId;
    }

    @Override
    public void setPickId(int pickId) {
        this.pickId = pickId;
    }

    @Override
    public String getRequestName() {
        return "VoidPickRequest";
    }

    @Override
    public String getResponseName() {
        return "VoidPickResponse";
    }
}
