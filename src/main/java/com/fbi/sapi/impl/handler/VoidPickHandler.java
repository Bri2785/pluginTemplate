package com.fbi.sapi.impl.handler;

import com.evnt.common.exceptions.FBException;
import com.evnt.eve.modules.logic.pick.LogicPick;
import com.fbi.fbdata.picking.PickFpo;
import com.fbi.fbo.impl.message.request.VoidPickRequestImpl;
import com.fbi.fbo.impl.message.response.MasterResponseImpl;
import com.fbi.fbo.impl.message.response.VoidPickResponseImpl;
import com.fbi.fbo.message.Response;
import com.fbi.fbo.message.request.VoidPickRequest;
import com.fbi.fbo.message.response.VoidPickResponse;
import com.fbi.fbo.picking.Pick;
import com.fbi.fbo.picking.PickItem;
import com.fbi.sdk.constants.pick.PickItemStatusConst;
import com.fbi.sdk.constants.pick.PickItemTypeConst;
import com.fbi.sdk.constants.pick.PickTypeConst;
import com.fbi.util.FbiException;
import com.fbi.util.UtilXML;
import com.fbi.util.logging.FBLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("VoidPickRq")
public class VoidPickHandler extends Handler{

    private static final Logger LOGGER = LoggerFactory.getLogger( VoidPickHandler.class);

@Autowired
    LogicPick logicPick;

    @Override
    public void execute(final String request, final int userId, final Response response) {

        MasterResponseImpl masterResponse = (MasterResponseImpl) response;

        final VoidPickResponse voidPickResponse = new VoidPickResponseImpl();

        masterResponse.getResponseList().add(voidPickResponse);

        try {
            final VoidPickRequest voidPickRequest = (VoidPickRequest) UtilXML.getObject(request, VoidPickRequestImpl.class);

            int pickId = voidPickRequest.getPickId();

            if (pickId <= 0){
                throw new FbiException("Pick Id is invalid");
            }

            Pick pickToVoid = logicPick.getPick(pickId);
            PickFpo pickFpoToVoid = logicPick.getPickFpo(pickId);

            //error already thrown by LogicPick class
//            if (pickToVoid == null){
//                throw new FbiException("Pick not found");
//            }

            //check for BTo items and bail if found
            boolean hasBTOItem = false;
            for (final PickItem item : pickToVoid.getItemsList()) {
                if (item.getPickItemType() == PickItemTypeConst.BTO) {
                    hasBTOItem = true;
                }
            }
            if (hasBTOItem) {
                throw  new FbiException("Pick has BTO items. This must be voided by a Fishbowl Client");
            }

            final Map<String, Object> detailMap = this.logicPick.voidPick(pickFpoToVoid);
            Pick voidedPick = (Pick) detailMap.get("Pick");

            String unvoidableItems = checkForUnvoidable(voidedPick.getItemsList(), voidedPick.getTypeID());

            if (unvoidableItems != null && !unvoidableItems.isEmpty()) {
                voidPickResponse.setUnvoidableItems(unvoidableItems);
            }

            voidPickResponse.setPick(voidedPick);

        }
        catch (FbiException | FBException var8) {
            FBLogger.error(var8.getMessage(), var8);
            voidPickResponse.setStatusCode(9020);
            voidPickResponse.setStatusMessage(var8.getMessage());

        }

    }

    private String checkForUnvoidable(final ArrayList<PickItem> items, final int pickType) {
        final List<String> shippedList = new ArrayList<String>();
        for (final PickItem pickItem : items) {
            if (pickItem.getStatus() == PickItemStatusConst.FINISHED) {
                shippedList.add(pickItem.getPart().getNum());
            }
        }
        if (!shippedList.isEmpty()) {
            StringBuilder msg = new StringBuilder("The following Pick Item(s) are either packed, shipped, or manufactured and cannot be voided:");
            if (pickType != PickTypeConst.PICK.getId()) {
                msg = new StringBuilder("Finished Pick Item(s) from a Transfer Order can not be voided:");
            }
            if (shippedList.size() > 20) {
                msg = new StringBuilder("Some of the pick items were unable to be voided because they are either packed, shipped, or manufactured.");
            }
            else {
                for (final String partNum : shippedList) {
                    msg.append("\n").append(partNum);
                }
            }
            return msg.toString();
        }
        return null;
    }

}
