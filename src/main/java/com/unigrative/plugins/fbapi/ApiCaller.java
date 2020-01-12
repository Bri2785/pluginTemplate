// 
// Decompiled by Procyon v0.5.30
// 

package com.unigrative.plugins.fbapi;

import com.fbi.fbo.impl.ApiCallType;
import com.fbi.fbo.message.request.RequestBase;
import com.fbi.fbo.message.response.ResponseBase;
import com.unigrative.plugins.exception.FishbowlException;

@FunctionalInterface
public interface ApiCaller
{
    ResponseBase call(final ApiCallType p0, final RequestBase p1) throws FishbowlException;
}
