package com.fbi.fbo.impl.message.response;

import com.fbi.fbo.message.response.GetPartResponse;

public class GetPartResponseImpl extends ResponseBaseImpl implements GetPartResponse{

//    private Part part;
//    //public static final String JiBX_bindingList = "|com.fbi.fbo.impl.wo.JiBX_bindings_objectsFactory|";
//
//    @Override
//    public Part getPart() {
//        return this.part;
//    }
//
//    @Override
//    public void setPart(final Part part) {
//        this.part = part;
//    }
    private String TestString;

    @Override
    public String getTestString() {
        return TestString;
    }

    @Override
    public void setTestString(String testString) {
        this.TestString = testString;
    }



}
