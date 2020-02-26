package com.fbi.fbo.impl.message.response;

import com.fbi.fbo.message.response.VoidPickItemsResponse;
import com.fbi.fbo.picking.Pick;

public class VoidPickItemsResponseImpl extends ResponseBaseImpl implements VoidPickItemsResponse {

    private Pick pick;
    private String unVoidableItems;

    @Override
    public Pick getPick() {
        return pick;
    }

    @Override
    public void setPick(Pick pick) {
        this.pick = pick;
    }

    @Override
    public String getUnvoidableItems() {
        return this.unVoidableItems;
    }

    @Override
    public void setUnvoidableItems(String items) {
        this.unVoidableItems = items;
    }

}
