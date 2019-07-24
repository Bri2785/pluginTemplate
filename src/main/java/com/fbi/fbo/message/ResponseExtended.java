package com.fbi.fbo.message;

import com.fbi.fbo.message.response.GetPartResponse;

public interface ResponseExtended extends Response {

    GetPartResponse appendGetPartResponse();
}
