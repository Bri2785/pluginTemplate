package com.fbi.fbo.impl.message.request;

import com.fbi.fbo.message.request.part.PartGetRequest;
import org.jibx.runtime.IMarshallable;
import org.jibx.runtime.IUnmarshallable;

public class GetPartRequestImpl extends RequestBaseImpl implements PartGetRequest  {

    private String number;
    private boolean getImage;

    //public static final String JiBX_bindingList = "|com.fbi.fbo.impl.wo.JiBX_bindings_objectsFactory|";

    @Override
    public String getRequestName() {
        return "GetPartRequest";
    }

    @Override
    public String getResponseName() {
        return "GetPartResponse";
    }

    @Override
    public String getNumber() {
        return this.number;
    }

    @Override
    public void setNumber(final String number) {
        this.number = number;
    }

    @Override
    public boolean getGetImage() {
        return this.getImage;
    }

    @Override
    public void setGetImage(final boolean getImage) {
        this.getImage = getImage;
    }


}
