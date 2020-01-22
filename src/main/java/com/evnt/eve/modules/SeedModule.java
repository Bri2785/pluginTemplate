package com.evnt.eve.modules;



import com.evnt.common.MethodConst;
import com.evnt.common.exceptions.FBException;
import com.evnt.eve.annotations.ClientCalled;
import com.evnt.eve.event.EVEvent;
import com.evnt.eve.event.EVEventUtil;
import com.evnt.eve.modules.logic.extra.LogicSeed;
import com.unigrative.plugins.models.Seed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.annotation.Annotation;

@Service
@Controller
public class SeedModule extends BaseModule  {

    @Autowired
    private LogicSeed logicSeed;
    
    @ClientCalled(methodName = MethodConst.UNKNOWN)
    public EVEvent updateSeed(final EVEvent event) {
        final EVEvent response = EVEventUtil.createResponse(event, 102);
        try {
            Seed seed = event.getObject("Seed", Seed.class);
            seed = this.logicSeed.updateSeed(seed);
            response.addObject((Object)"Seed", seed);
            return response;
        }
        catch (FBException e) {
            return EVEventUtil.exceptionEvent(event, e);
        }
    }

    @Override
    public String getModuleName() {
        return "Seed";
    }


}
