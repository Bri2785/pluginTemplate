package com.fbi.fbo.message.response;

import com.fbi.fbo.part.Part;

public interface GetPartResponse extends ResponseBase {

    public static final String BEAN_NAME = "GetPartResponse";

//    Part getPart();
//
//    void setPart(final Part p0);
    String getTestString() ;

    void setTestString(String testString);
}
