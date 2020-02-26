package com.fbi.fbo.impl.message.request;

import com.fbi.fbo.message.request.VoidPickItemsRequest;
import com.fbi.fbo.picking.Pick;
import com.fbi.fbo.picking.PickItem;

import java.util.List;

public class VoidPickItemsRequestImpl extends  RequestBaseImpl implements VoidPickItemsRequest {

    private Pick pick;
    private List<PickItem> itemList;

    public String getRequestName() {
        return "VoidPickItemsRequest";
    }

    public String getResponseName() {
        return "VoidPickItemsResponse";
    }

    @Override
    public Pick getPick() {
        return pick;
    }

    @Override
    public void setPick(Pick pick) {
        this.pick = pick;
    }

    @Override
    public List<PickItem> getItemList() {
        return itemList;
    }

    @Override
    public void setItemList(List<PickItem> itemList) {
        this.itemList = itemList;
    }
}
