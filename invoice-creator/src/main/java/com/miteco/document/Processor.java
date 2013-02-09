package com.miteco.document;

import com.itextpdf.text.DocumentException;
import com.miteco.document.InvoiceFactory;
import com.miteco.document.xml.model.AcceptanceProtocol;
import com.miteco.document.xml.model.Configuration;
import com.miteco.document.xml.model.Invoice;
import com.miteco.util.Constants;
import com.miteco.util.FileUtil;
import com.miteco.util.NumberConverter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


public class Processor {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    static Logger log = Logger.getLogger(Processor.class.getName());

    public void run() throws IOException, DocumentException {

        loadConfiguration();

        process();

        exitApp();

    }


    private void loadConfiguration() {


        try {

            // Load log4j.properties
            File log4jfile = new File("./conf/" + Constants.CONF_LOG4J_PROP_FILENAME);

            if (log4jfile.exists())
                PropertyConfigurator.configure(log4jfile.getAbsolutePath());
            else {
                // if not loaded then load log4j from jar
                Properties log4jProps = new Properties();
                log4jProps.load(getClass().getResourceAsStream("/conf/" + Constants.CONF_LOG4J_PROP_FILENAME));
                PropertyConfigurator.configure(log4jProps);
                log.warn(String.format("File '%s' not found. File is loaded from the jar.", Constants.CONF_LOG4J_PROP_FILENAME));
            }

            log.info("Application started.");

            setCurrentDirCanonicalPath();
            log.debug(String.format("Current directory is set to '%s'", getCurrentDirCanonicalPath()));

            log.info("Loading settings started.");

            // Load app.properties
            File propFile = new File("./conf/", Constants.CONF_APP_PROP_FILENAME);

            if (propFile.exists()) {
                this.appProperties = new Properties();
                this.appProperties.load(new FileInputStream(propFile));
                log.debug(String.format("File '%s' loaded.", Constants.CONF_APP_PROP_FILENAME));
            } else
                error(String.format("Missing file '%s'.", Constants.CONF_APP_PROP_FILENAME));

            for (String key : getAppProperties().stringPropertyNames()) {
                log.debug(String.format("Property '%s' loaded with value '%s'", key, getPropertyValue(key)));
            }

            // Set property values
            setOutputDirPath(getPropertyValue(Constants.PROP_OUTPUT_DIR));
            log.debug(String.format("Directory to generate sources is set to '%s'", getOutputDirCanonicalPath()));

            // Load app-config.xml
            XStream xs = new XStream();
            xs.processAnnotations(Configuration.class);

            String[] formats = {"yyyy-MM-dd"};
            xs.registerConverter(new DateConverter("yyyy-MM-dd", formats));
            File invConfigFile = new File("./conf/", Constants.CONF_INV_PROP_FILENAME);
            if (invConfigFile.exists())
                log.debug(String.format("File '%s' loaded.", Constants.CONF_INV_PROP_FILENAME));
            else
                error(String.format("Missing file '%s'.", Constants.CONF_INV_PROP_FILENAME));

            // Deserializing an configuration object from XML
            Configuration config = (Configuration) xs.fromXML(invConfigFile);
            if (config != null) setConfiguration(config);
            else
                error(String.format("File '%s' has not been parsed.", Constants.CONF_INV_PROP_FILENAME));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            exitApp();
        }

        log.info("Settings loaded.");

    }

    private void completeInvoiceData(Invoice invoice) {
        if (invoice.getIssuingDate() == null) {
            invoice.setIssuingDate(new Date());
        }

        GregorianCalendar calendar = new GregorianCalendar();
        if (invoice.getSellingDate() == null) {
            calendar.setTime(invoice.getIssuingDate());
            if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
                calendar.add(Calendar.DAY_OF_MONTH, -(calendar.get(Calendar.DAY_OF_MONTH)));
                calendar = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            }
            invoice.setSellingDate(calendar.getTime());
        }

        if ((invoice.getInvoiceNumber() == null) || (invoice.getInvoiceNumber().equals(""))) {
            calendar.setTime(invoice.getSellingDate());
            invoice.setInvoiceNumber(String.format("1/%s/%s", NumberConverter.convertMonthValueToString(calendar.get(Calendar.MONTH)+1), calendar.get(Calendar.YEAR)));
        }

        if (invoice.getPaymentDate() == null) {
            calendar.setTime(invoice.getIssuingDate());
            calendar.add(Calendar.DAY_OF_MONTH, 30);
            invoice.setPaymentDate(calendar.getTime());
        }

    }

    private void completeAcceptanceProtocolData(AcceptanceProtocol acceptanceProtocol) {

    }

    private void process() throws IOException, DocumentException {

        List<Invoice> invoices = getConfiguration().getInvoices();

        InvoiceFactory invoiceFactory = new InvoiceFactory();

        for (Invoice invoice : invoices) {

            completeInvoiceData(invoice);

            java.util.List<ByteArrayOutputStream> baosList = invoiceFactory.create(invoice);
            int k = 0;
            for (ByteArrayOutputStream baos : baosList) {
                String prefix = "MITECO_FV_";
                String postfix = "_";
                if (invoice.split())
                    postfix += (k < 1) ? Constants.INV_ORIGINAL_ENG : Constants.INV_COPY_ENG;
                else
                    postfix = "";
                k++;
                String fileName = getOutputDirCanonicalPath() + "/" + prefix + invoice.getInvoiceNumber().replaceAll("[^\\w\\[\\]\\(\\)]", "_") + postfix + ".pdf";
                // write the file from memory to a file
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(baos.toByteArray());
                log.info(String.format("Created file %s.", fileName));
                FileUtil.open(fileName);
                fos.close();


            }


        }

        AcceptanceProtocol acceptanceProtocol = getConfiguration().getAcceptanceProtocol();
        AcceptanceProtocolFactory acceptanceProtocolFactory = new AcceptanceProtocolFactory();

        completeAcceptanceProtocolData(acceptanceProtocol);

        java.util.List<ByteArrayOutputStream> baosList = acceptanceProtocolFactory.create(acceptanceProtocol);
        String prefix = "MITECO_PO_";

        for (ByteArrayOutputStream baos : baosList) {
            String fileName = getOutputDirCanonicalPath() + "/" + prefix + sdf.format(acceptanceProtocol.getIssuingDate()).replaceAll("[^\\w\\[\\]\\(\\)]", "_") + ".pdf";
            // write the file from memory to a file
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(baos.toByteArray());
            log.info(String.format("Created file %s.", fileName));

            FileUtil.open(fileName);

            fos.close();


        }

    }


    public String getCurrentDirCanonicalPath() {
        return currentDirCanonicalPath;
    }

    private String currentDirCanonicalPath;

    private void setCurrentDirCanonicalPath() throws IOException {
        this.currentDirCanonicalPath = new File(".").getCanonicalPath();
    }

    public Properties getAppProperties() {
        return appProperties;
    }

    private Properties appProperties;

    public Configuration getConfiguration() {
        return configuration;
    }

    protected void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    private Configuration configuration;

    private void exitApp() {
        log.info("Application closed.");
        log.info("...................");
        System.exit(-1);
    }

    private void error(String errorMessage) {
        log.error(errorMessage);
        exitApp();
    }

    private String getPropertyValue(String propertyName) {
        return getAppProperties().getProperty(propertyName);
    }


    public String getOutputDirPath() {
        return outputdDirPath;
    }

    public void setOutputDirPath(String outputDirPath) throws IOException {
        this.outputdDirPath = outputDirPath;
        setOutputDirCanonicalPath();
    }

    private String outputdDirPath;

    private String outputDirCanonicalPath;

    private void setOutputDirCanonicalPath() throws IOException {
        this.outputDirCanonicalPath = new File(getOutputDirPath()).getCanonicalPath();
    }

    public String getOutputDirCanonicalPath() {
        return outputDirCanonicalPath;
    }

}
