package com.fbi.sapi.impl.handler;

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
import com.fbi.util.FbiException;
import com.fbi.util.UtilXML;
import com.fbi.util.exception.ExceptionMainFree;
import com.fbi.util.logging.FBLogger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;
import org.springframework.stereotype.Service;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.OrientationRequested;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("PrintReportToPrinterRq")
public class PrintReportToPrinterHandler extends Handler {

    @Override
    public void execute(final String request, final int userId, final Response response) {

        MasterResponseImpl masterResponse = (MasterResponseImpl) response;

        final PrintReportToPrinterResponse printReportToPrinterResponse = new PrintReportToPrinterResponseImpl();

        masterResponse.getResponseList().add(printReportToPrinterResponse);

        try {
            final PrintReportToPrinterRequest printerRequest = (PrintReportToPrinterRequest) UtilXML.getObject(request, PrintReportToPrinterRequestImpl.class);

            if (Util.isEmpty(printerRequest.getReportName())) {
                throw new FbiException("Report Name is Required");
            }

            if (Util.isEmpty(printerRequest.getPrinterName())) {
                throw new FbiException("Printer Name is Required");
            }

            //locate the printer on the machine
            PrintService selectedService = getMatchingPrintService(printerRequest.getPrinterName());


            if (selectedService == null) {
                refreshSystemPrinterList(); //try to refresh it first
                selectedService = getMatchingPrintService(printerRequest.getPrinterName());
            }

            if (selectedService == null) { //still cant find so throw error
                throw new FbiException("Printer requested not found. Check printer name or installation");
            }


            final HashMap<String, Object> requestParamList = new HashMap<String, Object>();

            //get the parameters from the request and add them to the list
            for (final ReportParam reportParam : printerRequest.getParamList()) {
                requestParamList.put(reportParam.getName(), reportParam.getValue());
            }

            ReportFpo requestReport = this.getReport(printerRequest.getReportName());
            JasperPrint jasperPrint = this.loadAndCompileReport(requestReport, requestParamList);
            //system print method, only prints to default printer
            //LogicReport.printReport(jasperPrint, printerRequest.getPrinterName(), true,true,false);

            //custom exporter print method

            //number of copies is optional
            int numberOfCopies = 1;
            if (printerRequest.getNumberOfCopies() > 0){
                numberOfCopies = printerRequest.getNumberOfCopies();
            }
            printReport(jasperPrint, selectedService, numberOfCopies);


//            this.getDataManager().getReportLogic().mergeParameters(reportFpo, null);
//
//
//            //check to make sure the parameters supplied in the request are in the report
//            for (final Map.Entry<String, ReportParameterFpo> parameter : reportFpo.getParameterMap().entrySet()) {
//                if (!requestParamList.containsKey(parameter.getKey()) && !parameter.getKey().equalsIgnoreCase("REPORT_CONTEXT")) {
//                    requestParamList.put(parameter.getKey(), this.formatValue(parameter.getValue()));
//                }
//            }
//
//            //print the report
//            final JasperPrint jasperPrint = this.getDataManager().getReportLogic().getJasperPrint(reportFpo, requestParamList);
//            this.printReport(jasperPrint, selectedService, printerRequest.getNumberOfCopies(), true, false);

            //JasperPrintManager.printReport(jasperPrint, false);

        } catch (JRException e) {// | PrinterException e){
            FBLogger.error(e.getMessage(), e);
        } catch (FbiException e) {
            FBLogger.error(e.getMessage(), e);
            printReportToPrinterResponse.setStatusCode(e.getStatusCode());
            printReportToPrinterResponse.setStatusMessage(e.getMessage());
        } catch (ExceptionMainFree e2) {
            FBLogger.error(e2.getMessage(), e2);
            printReportToPrinterResponse.setStatusCode(e2.getCode());
            printReportToPrinterResponse.setStatusMessage(e2.getMsgErr());
        }

    }

    private ReportFpo getReport(String reportName) throws FbiException {

        //trying the report module way to load and populate the reports

        List<ReportFpo> reportSearchResults = this.getReportRepository().searchReports(reportName, null, null, 0);

        if (reportSearchResults.size() != 1) {
            //none or more than one returned
            throw new FbiException("No report or more than one report found matching this name");
        }

        int reportID = reportSearchResults.get(0).getId();

        final Map<String, String> userDefaultParameters = this.getDataManager().getReportLogic().getUserDefaultParameters();
        return this.getDataManager().getReportLogic().getReport(reportID, userDefaultParameters);


    }

    private JasperPrint loadAndCompileReport(ReportFpo jasperReport, final Map<String, Object> parameterOverrides) throws FbiException {

        if (parameterOverrides != null) {
            for (final Map.Entry<String, Object> entry : parameterOverrides.entrySet()) {
                if (jasperReport.getParameterMap().containsKey(entry.getKey())) {
                    if (entry.getValue() instanceof Date) {
                        final String date = Util.format((Date) entry.getValue(), 2);
                        jasperReport.getParameterMap().get(entry.getKey()).setValue(date);
                    } else {
                        jasperReport.getParameterMap().get(entry.getKey()).setValue(entry.getValue().toString());
                    }
                } else if (entry.getKey().startsWith("dateRangeValue")) {
                    jasperReport.getParameterMap().get("dateRange1" + entry.getKey().substring("dateRangeValue".length())).setDateRange(entry.getValue().toString());
                } else if (entry.getKey().startsWith("productTree3")) {
                    final ReportParameterFpo mainParamater = jasperReport.getParameterMap().get("productTree1" + entry.getKey().substring("productTree3".length()));
                    mainParamater.setValue(entry.getValue().toString());
                } else if (entry.getKey().startsWith("sortingTree")) {
                    final ReportParameterFpo mainParamater = jasperReport.getParameterMap().get("sort");
                    mainParamater.setValue(entry.getValue().toString());
                } else if (entry.getKey().startsWith("shipNum")) {
                    final ReportParameterFpo reportParameter = jasperReport.getParameterMap().get("shipID");
                    reportParameter.setValue(reportParameter.getValue() + ";" + entry.getValue().toString());
                } else {
                    if (!entry.getKey().startsWith("receiptNum")) {
                        continue;
                    }
                    final ReportParameterFpo reportParameter = jasperReport.getParameterMap().get("receiptID");
                    reportParameter.setValue(reportParameter.getValue() + ";" + entry.getValue().toString());
                }
            }
        }

        //DlgReportParameter dlgParams = new DlgReportParameter(jasperReport, new HashMap<>(), parameterOverrides, true, null );
        //this sets all parameters and our parameters for the report
        //if (dlgParams.isSuccess()){
        //compiles the report into the jpPrint object to be printed/exported
        return this.getDataManager().getReportLogic().getJasperPrint(jasperReport, parameterOverrides);

        //}
        //return null;
    }

    private void printReport(JasperPrint printObject, PrintService printService, int NumOfCopies) throws JRException {
        //print using the exporter so we can control more attributes

        //code to show the available paper sizes if needed
//        Media[] res = (Media[]) printService.getSupportedAttributeValues(Media.class, null, null);
//        for (Media media : res) {
//            if (media instanceof MediaSizeName) {
//                MediaSizeName msn = (MediaSizeName) media;
//                MediaSize ms = MediaSize.getMediaSizeForName(msn);
//                float width = ms.getX(MediaSize.INCH);
//                float height = ms.getY(MediaSize.INCH);
//                System.out.println(media + ": width = " + width + "; height = " + height);
//            }
//        }


        //Set the printing settings
        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        //printRequestAttributeSet.add(MediaSizeName.);
        printRequestAttributeSet.add(new Copies(NumOfCopies));
        if (printObject.getOrientationValue() == net.sf.jasperreports.engine.type.OrientationEnum.LANDSCAPE) {
            printRequestAttributeSet.add(OrientationRequested.LANDSCAPE);
        } else {
            printRequestAttributeSet.add(OrientationRequested.PORTRAIT);
        }
//        PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
//        printServiceAttributeSet.add(new PrinterName(selectedPrinter, null));

        JRPrintServiceExporter exporter = new JRPrintServiceExporter();
        SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
        configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
//        configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
        configuration.setDisplayPageDialog(false);
        configuration.setDisplayPrintDialog(false);
        configuration.setPrintService(printService);

        exporter.setExporterInput(new SimpleExporterInput(printObject));
        exporter.setConfiguration(configuration);

        exporter.exportReport();

    }

    private PrintService getMatchingPrintService(String serviceName){
        final PrintService[] lookupPrintServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (final PrintService service : lookupPrintServices) {
            FBLogger.debug(service.getName()); //log names
            if (service.getName().equalsIgnoreCase(serviceName)) {
                return service;

            }
        }
        return null;
    }

    public static void refreshSystemPrinterList() {

        Class[] classes = PrintServiceLookup.class.getDeclaredClasses();

        for (int i = 0; i < classes.length; i++) {

            if ("javax.print.PrintServiceLookup$Services".equals(classes[i].getName())) {

                sun.awt.AppContext.getAppContext().remove(classes[i]);
                break;
            }
        }
    }

}
