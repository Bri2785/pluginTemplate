package com.fbi.sapi.impl.handler;

import com.evnt.eve.modules.logic.extra.LogicReport;
import com.evnt.util.Util;
import com.fbi.fbdata.reports.ReportFpo;
import com.fbi.fbdata.reports.ReportParameterFpo;
import com.fbi.fbo.impl.message.request.PrintReportToPrinterRequestImpl;
import com.fbi.fbo.impl.message.response.MasterResponseImpl;
import com.fbi.fbo.impl.message.response.PrintReportToPrinterResponseImpl;
import com.fbi.fbo.message.Response;
import com.fbi.fbo.message.request.PrintReportToPrinterRequest;
import com.fbi.fbo.message.response.PrintReportToPrinterResponse;
import com.fbi.fbo.property.ReportParam;
import com.fbi.gui.util.UtilGui;
import com.fbi.util.FbiException;
import com.fbi.util.UtilXML;
import com.fbi.util.exception.ExceptionMainFree;
import com.fbi.util.logging.FBLogger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.stereotype.Service;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("PrintReportToPrinterRq")
public class PrintReportToPrinterHandler extends Handler {

    @Override
    public void execute(final String request, final int userId, final Response response) {

        MasterResponseImpl masterResponse = (MasterResponseImpl)response;

        final PrintReportToPrinterResponse printReportToPrinterResponse = new PrintReportToPrinterResponseImpl();

        masterResponse.getResponseList().add(printReportToPrinterResponse);

        try {
            final PrintReportToPrinterRequest printerRequest = (PrintReportToPrinterRequest) UtilXML.getObject(request, PrintReportToPrinterRequestImpl.class);

            if (Util.isEmpty(printerRequest.getReportName())){
                throw new FbiException("Report Name is Required");
            }

            if (Util.isEmpty(printerRequest.getPrinterName())){
                throw new FbiException("Printer Name is Required");
            }

            //locate the printer on the machine
            PrintService selectedService = null;
            final PrintService[] lookupPrintServices;
            final PrintService[] services = lookupPrintServices = PrintServiceLookup.lookupPrintServices(null, null);
            for (final PrintService service : lookupPrintServices) {
                if (service.getName().equalsIgnoreCase(printerRequest.getPrinterName())) {
                    selectedService = service;
                    break;
                }
            }

            if (selectedService == null){
                throw new FbiException("Printer requested not found. Check printer name or installation");
            }

            final HashMap<String, Object> paramList = new HashMap<String, Object>();

            //get the parameters from the request and add them to the list
            for (final ReportParam reportParam : printerRequest.getParamList()) {
                paramList.put(reportParam.getName(), reportParam.getValue());
            }

            List<ReportFpo> reportSearchResults = this.getReportRepository().searchReports(printerRequest.getReportName(), null, null, 0);

            if (reportSearchResults.size() != 1){
                //none or more than one returned
                throw new FbiException("No report or more than one report found matching this name");
            }

            ReportFpo reportFpo = reportSearchResults.get(0);
            this.getDataManager().getReportLogic().mergeParameters(reportFpo, null);


            //check to make sure the parameters supplied in the request are in the report
            for (final Map.Entry<String, ReportParameterFpo> parameter : reportFpo.getParameterMap().entrySet()) {
                if (!paramList.containsKey(parameter.getKey()) && !parameter.getKey().equalsIgnoreCase("REPORT_CONTEXT")) {
                    paramList.put(parameter.getKey(), this.formatValue(parameter.getValue()));
                }
            }

            //print the report
            final JasperPrint jasperPrint = this.getDataManager().getReportLogic().getJasperPrint(reportFpo, paramList);
            this.printReport(jasperPrint, selectedService, printerRequest.getNumberOfCopies(), true, false);

        }
        catch (FbiException e) {
            FBLogger.error((Object)e.getMessage(), (Throwable)e);
            printReportToPrinterResponse.setStatusCode(e.getStatusCode());
            printReportToPrinterResponse.setStatusMessage(e.getMessage());
        }
        catch (ExceptionMainFree e2) {
            FBLogger.error((Object)e2.getMessage(), (Throwable)e2);
            printReportToPrinterResponse.setStatusCode(e2.getCode());
            printReportToPrinterResponse.setStatusMessage(e2.getMsgErr());
        }

    }

    private Object formatValue(final ReportParameterFpo parameter) {
        final String value = parameter.getValue();
        try {
            switch (parameter.getPanelType()) {
                case 100: {
                    return new BigDecimal(value);
                }
                case 200: {
                    return Boolean.valueOf(value);
                }
                case 300: {
                    return Byte.valueOf(value);
                }
                case 400: {
                    final FastDateFormat df = FastDateFormat.getInstance("MM/dd/yyyy");
                    return df.parse(value);
                }
                case 600: {
                    return Float.valueOf(value);
                }
                case 800: {
                    return Integer.valueOf(value);
                }
                case 900: {
                    return Long.valueOf(value);
                }
                case 1100: {
                    return Short.valueOf(value);
                }
                case 201: {
                    if ("TRUE".equalsIgnoreCase(value) || "FALSE".equalsIgnoreCase(value)) {
                        return Boolean.valueOf(value);
                    }
                    return value;
                }
            }
        }
        catch (NumberFormatException | ParseException e) {
            FBLogger.error((Object)e.getMessage(), (Throwable)e);
        }
        return value;
    }

    private void printReport(final JasperPrint jasperPrint, final PrintService selectedService, final int numberOfCopies, final boolean hidePrintOptions, final boolean isClientSide) throws  FbiException {

        final PrinterJob job = PrinterJob.getPrinterJob();

        try {
            job.setPrintService(selectedService);

            final JRPrintServiceExporter exporter = new JRPrintServiceExporter();

            final PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
            attributeSet.add(new Copies(numberOfCopies));

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, !hidePrintOptions);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, job.getPrintService());
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, job.getPrintService().getAttributes());

            exporter.setParameter((JRExporterParameter) JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, attributeSet);
            exporter.exportReport();
        }
        catch (JRException | PrinterException e){
            throw new FbiException("Error printing report", e);
        }
    }
}
