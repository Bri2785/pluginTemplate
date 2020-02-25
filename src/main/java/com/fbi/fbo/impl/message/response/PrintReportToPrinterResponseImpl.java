package com.fbi.fbo.impl.message.response;

import com.fbi.fbo.message.response.PrintReportToPrinterResponse;

public class PrintReportToPrinterResponseImpl extends ResponseBaseImpl implements PrintReportToPrinterResponse {

    private int jobId;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }
}
