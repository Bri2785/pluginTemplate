package com.unigrative.plugins.runnables;

import com.fbi.TimerTask.IFBRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class GenericScheduledTask implements IFBRunnable {

    //IF THE CLASS NAME OR PATH CHANGES, THE SERVER WILL NEED TO BE RESTARTED SO THE CLASS CAN BE SCANNED AND FOUND
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericScheduledTask.class);

    public GenericScheduledTask() {
    }

    @Override
    public void run(HashMap args) throws Exception {
        LOGGER.error("Scheduled Task has run");
    }
}
