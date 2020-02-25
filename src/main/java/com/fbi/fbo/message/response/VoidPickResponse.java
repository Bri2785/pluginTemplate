package com.fbi.fbo.message.response;

import com.fbi.fbo.picking.Pick;

public interface VoidPickResponse extends ResponseBase {

    public static final String BEAN_NAME = "VoidPickResponse";

    //return voided pick (fresh pick)

    Pick getPick();

    void setPick(Pick pick);

    String getUnvoidableItems();

    void setUnvoidableItems(String items);
}
