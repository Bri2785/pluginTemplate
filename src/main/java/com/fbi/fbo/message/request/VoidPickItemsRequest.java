package com.fbi.fbo.message.request;

import com.fbi.fbo.picking.Pick;
import com.fbi.fbo.picking.PickItem;

import java.util.List;

public interface VoidPickItemsRequest extends RequestBase{

    Pick getPick();

    void setPick(Pick pick);

    List<PickItem> getItemList();

    void setItemList(List<PickItem> itemList);
}
