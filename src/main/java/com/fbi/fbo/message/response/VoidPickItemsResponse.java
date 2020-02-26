package com.fbi.fbo.message.response;

import com.fbi.fbo.picking.Pick;

public interface VoidPickItemsResponse extends ResponseBase {

    Pick getPick();

    void setPick(Pick pick);

    String getUnvoidableItems();

    void setUnvoidableItems(String items);
}
