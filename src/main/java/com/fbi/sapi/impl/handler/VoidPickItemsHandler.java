package com.fbi.sapi.impl.handler;


import com.evnt.common.exceptions.FBException;
import com.evnt.eve.modules.logic.pick.LogicPick;
import com.evnt.util.Util;
import com.fbi.fbo.impl.message.request.VoidPickItemsRequestImpl;
import com.fbi.fbo.impl.message.response.MasterResponseImpl;
import com.fbi.fbo.impl.message.response.VoidPickItemsResponseImpl;
import com.fbi.fbo.impl.message.response.VoidPickResponseImpl;
import com.fbi.fbo.message.Response;
import com.fbi.fbo.message.request.VoidPickItemsRequest;
import com.fbi.fbo.message.response.VoidPickItemsResponse;
import com.fbi.fbo.message.response.VoidPickResponse;
import com.fbi.fbo.picking.Pick;
import com.fbi.fbo.picking.PickItem;
import com.fbi.sdk.constants.pick.PickItemStatusConst;
import com.fbi.sdk.constants.pick.PickItemTypeConst;
import com.fbi.sdk.constants.pick.PickTypeConst;
import com.fbi.util.FbiException;
import com.fbi.util.UtilXML;
import com.fbi.util.logging.FBLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("VoidPickItemsRq")
public class VoidPickItemsHandler extends Handler {

    @Autowired
    LogicPick logicPick;

    @Override
    public void execute(String request, int i, Response response) {

        MasterResponseImpl masterResponse = (MasterResponseImpl) response;

        final VoidPickItemsResponse voidPickItemsResponse = new VoidPickItemsResponseImpl();

        masterResponse.getResponseList().add(voidPickItemsResponse);


        try{
            final VoidPickItemsRequest voidPickItemsRequest = (VoidPickItemsRequest) UtilXML.getObject(request, VoidPickItemsRequestImpl.class);

            final Pick pick = voidPickItemsRequest.getPick();
            final List<PickItem> requestedItemsToVoid = voidPickItemsRequest.getItemList();


            boolean hasBTOItem = false;
            for (final PickItem item : requestedItemsToVoid) {
                if (item.getPickItemType() == PickItemTypeConst.BTO) {
                    hasBTOItem = true;
                }
            }
            if (hasBTOItem) {
                throw  new FbiException("Pick has BTO items. This must be voided by a Fishbowl Client");
            }

            final ArrayList<PickItem> itemsToVoid = new ArrayList<PickItem>();
            for (PickItem item :
                    requestedItemsToVoid) {
                if (item.getPickItemID() > 0) {
                    if (!this.isVoidable(item, pick)) {
                        continue;
                    }
                    itemsToVoid.add(item);
                }
            }

            if (Util.isEmpty(itemsToVoid)) {
                return;
            }
            logicPick.voidPickItems(itemsToVoid, pick);

            Pick reloadedPick = logicPick.getPick(pick.getPickID());

            final ArrayList<String> shippedList = new ArrayList<String>();
            for (final PickItem pickItem2 : reloadedPick.getItemsList()) {
                if (pickItem2.getStatus() == PickItemStatusConst.FINISHED && itemsToVoid.contains(pickItem2)) {
                    shippedList.add(pickItem2.getPart().getNum());
                }
            }
            if (!shippedList.isEmpty()) {
                final StringBuilder msg2 = new StringBuilder("The following Pick Item(s) are either packed, shipped, or manufactured and cannot be voided:");
                for (final String partNum : shippedList) {
                    msg2.append("\n").append(partNum);
                }
                voidPickItemsResponse.setUnvoidableItems(msg2.toString());
            }

            voidPickItemsResponse.setPick(reloadedPick);


        }
        catch (FbiException | FBException var8) {
            FBLogger.error(var8.getMessage(), var8);
            voidPickItemsResponse.setStatusCode(9030);
            voidPickItemsResponse.setStatusMessage(var8.getMessage());

        }


    }
    private boolean isVoidable(final PickItem item, Pick pick) {
        return !item.getStatus().isEntered() && item.getStatus() != PickItemStatusConst.SHORT && (pick == null || pick.getTypeID() == PickTypeConst.PICK.getId() || !item.getStatus().isFinished());
    }
}
