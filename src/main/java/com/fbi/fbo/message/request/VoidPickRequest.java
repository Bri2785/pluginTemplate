package com.fbi.fbo.message.request;

public interface VoidPickRequest extends RequestBase {

    public static final String BEAN_NAME = "VoidPickRequest";

    int getPickId();

    void setPickId(int pickId);
}
