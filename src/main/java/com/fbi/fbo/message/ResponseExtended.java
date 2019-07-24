package com.fbi.fbo.message;

import com.fbi.fbo.message.response.GetPartResponse;
import com.fbi.fbo.message.response.SaveSOWithCFResponse;

public interface ResponseExtended extends Response {

    GetPartResponse appendGetPartResponse();

    SaveSOWithCFResponse appendSaveSOWithCFResponse();
}
