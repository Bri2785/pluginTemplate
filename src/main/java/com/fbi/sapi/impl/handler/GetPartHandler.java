package com.fbi.sapi.impl.handler;

import com.fbi.fbo.impl.message.request.GetPartRequestImpl;
import com.fbi.fbo.impl.message.response.GetPartResponseImpl;
import com.fbi.fbo.impl.message.response.MasterResponseImpl;
import com.fbi.fbo.message.Response;
import com.fbi.fbo.message.ResponseExtended;
import com.fbi.fbo.message.request.GetPartRequest;
import com.fbi.fbo.message.response.GetPartResponse;
import com.fbi.fbo.part.Part;
import com.fbi.util.FbiException;
import com.fbi.util.UtilXML;
import com.fbi.util.exception.ExceptionMainFree;
import com.fbi.util.logging.FBLogger;
import org.springframework.stereotype.Service;

@Service("GetPartRq")
public class GetPartHandler extends Handler {

    @Override
    public void execute(final String request, final int userId, final Response response) {

        MasterResponseImpl masterResponse = (MasterResponseImpl)response;

        final GetPartResponse partRs = new GetPartResponseImpl();

        masterResponse.getResponseList().add(partRs);

        try {
            //final GetPartRequest partRq = (GetPartRequest)UtilXML.getObject(request, GetPartRequestImpl.class);
            final String testString = "test string";
            partRs.setTestString(testString);
//            final Part part = this.getInventoryManager().getPart(partRq.getNumber(), partRq.getGetImage());
//            partRs.setPart(part);
        }
//        catch (FbiException e) {
//            FBLogger.error((Object)e.getMessage(), (Throwable)e);
//            partRs.setStatusCode(e.getStatusCode());
//            partRs.setStatusMessage(e.getMessage());
//        }
        catch (ExceptionMainFree e2) {
            FBLogger.error((Object)e2.getMessage(), (Throwable)e2);
            partRs.setStatusCode(e2.getCode());
            partRs.setStatusMessage(e2.getMsgErr());
        }
    }
}
