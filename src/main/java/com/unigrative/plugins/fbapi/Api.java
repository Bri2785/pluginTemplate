// 
// Decompiled by Procyon v0.5.30
// 

package com.unigrative.plugins.fbapi;

import com.fbi.fbo.impl.ApiCallType;
import com.fbi.fbo.message.request.GetShipmentRequest;
import com.fbi.fbo.message.request.ImportRequest;
import com.fbi.fbo.message.request.RequestBase;
import com.fbi.fbo.message.request.SaveShipmentRequest;
import com.fbi.fbo.message.request.salesorder.AddSOItemRequest;
import com.fbi.fbo.message.response.GetShipmentResponse;
import com.fbi.fbo.message.response.ImportResponse;
import com.fbi.fbo.message.response.ResponseBase;
import com.fbi.fbo.message.response.SaveShipmentResponse;
import com.fbi.fbo.message.response.salesorder.AddSOItemResponse;
import com.unigrative.plugins.exception.FishbowlException;

public final class Api<T extends RequestBase, U extends ResponseBase>
{
    public static final Api<ImportRequest, ImportResponse> IMPORT;
    public static final Api<GetShipmentRequest, GetShipmentResponse> GET_SHIPMENT;
    public static final Api<SaveShipmentRequest, SaveShipmentResponse> SAVE_SHIPMENT;
    public static final Api<AddSOItemRequest, AddSOItemResponse> ADD_SO_ITEM;
    private final ApiCallType callType;
    
    private Api(final ApiCallType callType) {
        this.callType = callType;
    }
    
    public U call(final ApiCaller api, final T request) throws FishbowlException {
        return (U)api.call(this.callType, request);
    }
    
    static {
        IMPORT = new Api<ImportRequest, ImportResponse>(ApiCallType.IMPORT);
        GET_SHIPMENT = new Api<GetShipmentRequest, GetShipmentResponse>(ApiCallType.GET_SHIPMENT);
        SAVE_SHIPMENT = new Api<SaveShipmentRequest, SaveShipmentResponse>(ApiCallType.SAVE_SHIPMENT);
        ADD_SO_ITEM = new Api<AddSOItemRequest, AddSOItemResponse>(ApiCallType.ADD_SO_ITEM);
    }
}
