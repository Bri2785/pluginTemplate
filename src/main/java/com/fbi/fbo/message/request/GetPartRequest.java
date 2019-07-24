package com.fbi.fbo.message.request;

import com.fbi.fbo.message.request.RequestBase;

public interface GetPartRequest extends RequestBase {

    public static final String BEAN_NAME = "GetPartRequest";

    String getRequestName();

    String getResponseName();

    String getNumber();

    void setNumber(final String p0);

    boolean getGetImage();

    void setGetImage(final boolean p0);
}
