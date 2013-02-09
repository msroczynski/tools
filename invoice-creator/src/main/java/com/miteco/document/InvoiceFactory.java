package com.miteco.document;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.miteco.util.pdf.PdfDocument;
import com.miteco.util.NumberConverter;
import com.miteco.document.xml.model.Amount;
import com.miteco.document.xml.model.Buyer;
import com.miteco.document.xml.model.Invoice;
import com.miteco.document.xml.model.InvoiceLine;
import com.miteco.document.xml.model.Seller;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


public class InvoiceFactory extends DocumentFactory {

    static Logger log = Logger.getLogger(InvoiceFactory.class.getName());

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    private Invoice invoice;

    private boolean showBorder;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private void addContent(Document document) throws IOException, DocumentException {
        document.add(createHeader());
        document.add(createPartyInfo());
        document.add(createInvoiceLines());
        document.add(createInvoiceSummary());
        document.add(createSigns());
    }

    @Override
    public java.util.List<ByteArrayOutputStream> create(Object object) throws DocumentException, IOException {

        java.util.List<ByteArrayOutputStream> result = new ArrayList<ByteArrayOutputStream>();

        // TODO fix hardcoded object type
        setInvoice((com.miteco.document.xml.model.Invoice)object);

        // Create instance of PDF document
        PdfDocument document = new PdfDocument();
        document.setMargins(36, 36, 36, 36);

        // Create in memory
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        getInvoice().setAsOriginal();
        document.open();
        addContent(document);
        getInvoice().setAsCopy();

        if (getInvoice().split()) {

            log.info(String.format("Creating invoice %s in separated files (original & copy).", getInvoice().getInvoiceNumber()));
            result.add(baos);
            document.close();

            // Create new instance of PDF document
            document = new PdfDocument();
            document.setMargins(36, 36, 36, 36);

            // Create in memory
            baos = new ByteArrayOutputStream();
            writer = PdfWriter.getInstance(document, baos);
            document.open();
            addContent(document);
            result.add(baos);
            document.close();
        } else {
            log.info(String.format("Creating invoice %s in one file (original & copy).", getInvoice().getInvoiceNumber()));
            document.newPage();
            addContent(document);
            document.close();
            result.add(baos);
        }
        return result;
    }

    private PdfPTable createHeader() throws DocumentException, IOException {

        PdfPTable headerTable = new PdfPTable(3);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{4, 4, 4.3f});
        // TODO headerTable.set

        PdfPCell defaultCell = headerTable.getDefaultCell();
        if (!showBorder) defaultCell.setBorder(Rectangle.NO_BORDER);

        //Image logoImage = Image.getInstance("img/logo-miteco.png");
        //logoImage.scalePercent(50);

        BaseFont bfDoom = BaseFont.createFont(new File(".").getCanonicalPath()+"/font/DooM.ttf", BaseFont.CP1250, BaseFont.EMBEDDED);
        Font fDoom = new Font(bfDoom, 27);
        fDoom.setColor(BaseColor.GRAY);
        Paragraph logoParagraph = new Paragraph("MITECO",fDoom);

        //PdfPCell logoCell = new PdfPCell(logoImage);
        PdfPCell logoCell = new PdfPCell(logoParagraph);
        logoCell.setPaddingTop(0);
        logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        if (!showBorder) logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setRowspan(4);
        headerTable.addCell(logoCell);
        headerTable.addCell(defaultCell);

        String invoiceNumberText = String.format("Faktura VAT %s", getInvoice().getInvoiceNumber());

        Paragraph invoiceNumberParagraph = new Paragraph(invoiceNumberText, getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.WHITE));

        /* TODO calculate width
        BaseFont bfInvoiceNumber = helvetica16.getCalculatedBaseFont(false);
        float invoiceNumberTextWidth = bfInvoiceNumber.getWidthPoint("Faktura VAT", 16);
          */

        PdfPCell invoiceNumberCell = new PdfPCell(invoiceNumberParagraph);
        if (!showBorder) invoiceNumberCell.setBorder(Rectangle.NO_BORDER);

        invoiceNumberCell.setBackgroundColor(BaseColor.BLACK);
        invoiceNumberCell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        invoiceNumberCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //invoiceNumberCell.setPadding(2);
        //invoiceNumberCell.setPaddingRight(6);
        invoiceNumberCell.setPaddingBottom(6);
        invoiceNumberCell.setPaddingLeft(7);

        headerTable.addCell(invoiceNumberCell);
        headerTable.addCell(defaultCell);
        // TODO
        Paragraph InvoiceTypeParagraph = new Paragraph(getInvoice().getInvoiceKind().toUpperCase(), getFont(FontFactory.HELVETICA_BOLD, 12));

        //parInvoiceType.setSpacingBefore(15);
        PdfPCell invoiceTypeCell = new PdfPCell(InvoiceTypeParagraph);
        if (!showBorder) invoiceTypeCell.setBorder(Rectangle.NO_BORDER);

        invoiceTypeCell.setVerticalAlignment(Element.ALIGN_CENTER);
        invoiceTypeCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        headerTable.addCell(invoiceTypeCell);
        headerTable.addCell(defaultCell);

        PdfPCell issuingInfoCell = new PdfPCell();
        if (!showBorder) issuingInfoCell.setBorder(Rectangle.NO_BORDER);

        String issuingDateText = String.format("Data wystawienia: %s", sdf.format(getInvoice().getIssuingDate()));
        String sellingDateText = String.format("Data sprzedaży: %s", sdf.format(getInvoice().getSellingDate()));
        Paragraph issuingInfoParagraph = new Paragraph(issuingDateText, getFont(FontFactory.TIMES, 10));
        issuingInfoParagraph.setAlignment(Element.ALIGN_RIGHT);
        issuingInfoParagraph.add(Chunk.NEWLINE);
        issuingInfoParagraph.add(sellingDateText);
        issuingInfoCell.addElement(issuingInfoParagraph);

        PdfPCell emptyCell = headerTable.getDefaultCell();
        if (!showBorder) emptyCell.setBorder(Rectangle.NO_BORDER);
        emptyCell.setFixedHeight(10);
        headerTable.addCell(emptyCell);
        headerTable.addCell(emptyCell);
        headerTable.addCell(issuingInfoCell);

        headerTable.completeRow();

        return headerTable;

    }


    private PdfPTable createPartyInfo() throws DocumentException {

        PdfPTable partyInfoTable = new PdfPTable(3);
        partyInfoTable.setSpacingBefore(80);
        partyInfoTable.setWidthPercentage(100);
        partyInfoTable.setWidths(new int[]{10, 3, 10});

        PdfPCell sellerHeaderCell = new PdfPCell();
        PdfPCell sellerCell = new PdfPCell();
        PdfPCell buyerHeaderCell = new PdfPCell();
        PdfPCell buyerCell = new PdfPCell();
        PdfPCell middleCell = new PdfPCell();

        if (!showBorder) middleCell.setBorder(Rectangle.NO_BORDER);

        sellerHeaderCell.setBorder(Rectangle.BOTTOM);
        sellerHeaderCell.setBorderWidth(1);
        sellerHeaderCell.setBorderColor(BaseColor.GRAY);


        Paragraph sellerHdrParagraph = new Paragraph("Sprzedawca", getFont(FontFactory.TIMES_BOLD, 12));
        sellerHdrParagraph.setSpacingAfter(5);
        sellerHeaderCell.addElement(sellerHdrParagraph);

        if (!showBorder) sellerCell.setBorder(Rectangle.NO_BORDER);
        sellerCell.setHorizontalAlignment(Element.ALIGN_LEFT);

        if (getInvoice().hasSeller()) {
            Seller seller = getInvoice().getSeller();
            Paragraph sellerParagraph = new Paragraph(seller.getName(), getFont(FontFactory.TIMES_BOLD, 10));
            sellerParagraph.add(Chunk.NEWLINE);
            sellerParagraph.setFont(getFont(FontFactory.TIMES, 10));
            if (seller.hasAddress()) {
                sellerParagraph.add(String.format("%s %s", seller.getAddress().getPostalCode(), seller.getAddress().getCity()));
                sellerParagraph.add(Chunk.NEWLINE);
                sellerParagraph.add(String.format("%s %s", seller.getAddress().getStreet(), seller.getAddress().getBuildingNumber()));
            }
            sellerParagraph.add(Chunk.NEWLINE);
            sellerParagraph.add(String.format("NIP: %s", seller.getNip()));
            sellerCell.addElement(sellerParagraph);
        }

        buyerHeaderCell.setBorder(Rectangle.BOTTOM);
        buyerHeaderCell.setBorderWidth(1);
        buyerHeaderCell.setBorderColor(BaseColor.GRAY);
        buyerHeaderCell.addElement(new Paragraph("Nabywca", getFont(FontFactory.TIMES_BOLD, 12)));

        if (!showBorder) buyerCell.setBorder(Rectangle.NO_BORDER);
        buyerCell.setHorizontalAlignment(Element.ALIGN_LEFT);

        if (getInvoice().hasBuyer()) {
            Buyer buyer = getInvoice().getBuyer();
            Paragraph buyerParagraph = new Paragraph(buyer.getName(), getFont(FontFactory.TIMES_BOLD, 10));
            buyerParagraph.add(Chunk.NEWLINE);
            buyerParagraph.setFont(getFont(FontFactory.TIMES, 10));
            if (buyer.hasAddress()) {
                buyerParagraph.add(String.format("%s %s", buyer.getAddress().getPostalCode(), buyer.getAddress().getCity()));
                buyerParagraph.add(Chunk.NEWLINE);
                buyerParagraph.add(String.format("%s %s", buyer.getAddress().getStreet(), buyer.getAddress().getBuildingNumber()));
            }
            buyerParagraph.add(Chunk.NEWLINE);
            buyerParagraph.add(String.format("NIP: %s", buyer.getNip()));
            buyerCell.addElement(buyerParagraph);
        }


        partyInfoTable.addCell(sellerHeaderCell);
        partyInfoTable.addCell(middleCell);
        partyInfoTable.addCell(buyerHeaderCell);
        partyInfoTable.addCell(sellerCell);
        partyInfoTable.addCell(middleCell);
        partyInfoTable.addCell(buyerCell);

        return partyInfoTable;

    }


    private PdfPTable createInvoiceLines() throws DocumentException {


        PdfPTable invoiceLinesTable = new PdfPTable(8);
        invoiceLinesTable.setSpacingBefore(75);
        invoiceLinesTable.setWidthPercentage(100);
        invoiceLinesTable.setWidths(new int[]{10, 100, 15, 20, 15, 20, 20, 20});

        java.util.List<String> invoiceLinesHeaderList = new ArrayList<String>();
        invoiceLinesHeaderList.add("Lp.");
        //invoiceLinesHeaderList.add("Symbol");
        invoiceLinesHeaderList.add("Nazwa");
        //invoiceLinesHeaderList.add("PKWiU");
        //invoiceLinesHeaderList.add("J.m.");
        invoiceLinesHeaderList.add("Ilość");
        invoiceLinesHeaderList.add("Cena jedn. netto");
        invoiceLinesHeaderList.add("Stawka VAT");
        invoiceLinesHeaderList.add("Wartość netto");
        invoiceLinesHeaderList.add("Wartość VAT");
        invoiceLinesHeaderList.add("Wartość brutto");


        for (String text : invoiceLinesHeaderList) {
            PdfPCell invoiceLineHdrCell = new PdfPCell(new Paragraph(text, getFont(FontFactory.TIMES_BOLD, 8)));
            invoiceLineHdrCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            invoiceLineHdrCell.setPaddingBottom(5);
            invoiceLineHdrCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            invoiceLineHdrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            invoiceLinesTable.addCell(invoiceLineHdrCell);
        }

        ArrayList invoiceLinesList = new ArrayList();

        getInvoice().calculateTotal();

        for (InvoiceLine line : getInvoice().getInvoiceLines()) {
            java.util.List<String> lineList = new ArrayList<String>();
            lineList.add("" + line.getLineNumber());
            lineList.add(line.getName());
            lineList.add("" + line.getUnitsNumber());
            lineList.add(NumberConverter.convertValueToString(line.getNetUnitPrice().doubleValue(), false));
            lineList.add(String.format("%s%s", line.getVatRate(), "%"));

            lineList.add(NumberConverter.convertValueToString(line.getNetValue().doubleValue(), false));
            lineList.add(NumberConverter.convertValueToString(line.getVatValue().doubleValue(), false));
            lineList.add(NumberConverter.convertValueToString(line.getGrossValue().doubleValue(), false));
            /*
            lineList.add(""+line.getNetValue());
            lineList.add(""+line.getVatValue());
            lineList.add(""+line.getGrossValue()); */
            invoiceLinesList.add(lineList);
        }

        // Invoice lines
        for (int i = 0; i < invoiceLinesList.size(); i++) {
            java.util.List<String> line = (java.util.List) invoiceLinesList.get(i);
            int k = 0;
            for (String text : line) {
                k++;
                int alignment = Element.ALIGN_CENTER;
                if (k == 2) alignment = Element.ALIGN_LEFT;
                PdfPCell invoiceLineCell = new PdfPCell(new Paragraph(text, getFont(FontFactory.TIMES, 8)));
                invoiceLineCell.setPaddingBottom(5);
                invoiceLineCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                invoiceLineCell.setHorizontalAlignment(alignment);
                invoiceLinesTable.addCell(invoiceLineCell);
            }

        }

        // Invoice lines summary
        ArrayList invoiceLinesSummaryList = new ArrayList();

        for (Integer vatRateAmount : getInvoice().getSummaryPerVatRateMap().keySet()) {
            java.util.List<String> summaryVatRateList = new ArrayList<String>();
            summaryVatRateList.add(String.format("%s%s", vatRateAmount, "%"));
            Amount summaryVatRateAmount = getInvoice().getSummaryPerVatRateMap().get(vatRateAmount);

            summaryVatRateList.add(NumberConverter.convertValueToString(summaryVatRateAmount.getNet().doubleValue(), false));
            summaryVatRateList.add(NumberConverter.convertValueToString(summaryVatRateAmount.getVat().doubleValue(), false));
            summaryVatRateList.add(NumberConverter.convertValueToString(summaryVatRateAmount.getGross().doubleValue(), false));
            /*
            summaryVatRateList.add(""+summaryVatRateAmount.getNet());
            summaryVatRateList.add(""+summaryVatRateAmount.getVat());
            summaryVatRateList.add(""+summaryVatRateAmount.getGross()); */
            invoiceLinesSummaryList.add(summaryVatRateList);
        }



        java.util.List<String> summaryItemAllList = new ArrayList<String>();
        summaryItemAllList.add("Razem:");
        Amount totalAmount = getInvoice().getSummaryTotal();
        summaryItemAllList.add(NumberConverter.convertValueToString(totalAmount.getNet().doubleValue(), false));
        summaryItemAllList.add(NumberConverter.convertValueToString(totalAmount.getVat().doubleValue(), false));
        summaryItemAllList.add(NumberConverter.convertValueToString(totalAmount.getGross().doubleValue(), false));

        /*
        summaryItemAllList.add(""+totalAmount.getNet());
        summaryItemAllList.add(""+totalAmount.getVat());
        summaryItemAllList.add(""+totalAmount.getGross()); */
        invoiceLinesSummaryList.add(summaryItemAllList);

        PdfPCell emptyCell = invoiceLinesTable.getDefaultCell();
        emptyCell.setBorder(Rectangle.NO_BORDER);

        for (int i = 0; i < invoiceLinesSummaryList.size(); i++) {
            java.util.List<String> line = (java.util.List<String>) invoiceLinesSummaryList.get(i);

            for (int k = 0; k < 4; k++) {
                invoiceLinesTable.addCell(emptyCell);
            }

            int l = 0;
            for (String text : line) {
                int alignment = Element.ALIGN_CENTER;
                l++;
                // TODO line.contains("Razem") change font to bold

                PdfPCell invoiceLineCell = new PdfPCell(new Paragraph(text, getFont(FontFactory.TIMES, 8)));
                invoiceLineCell.setPaddingBottom(4);
                invoiceLineCell.setHorizontalAlignment(alignment);
                invoiceLineCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                if (l == 1) invoiceLineCell.setBorder(Rectangle.NO_BORDER);
                invoiceLinesTable.addCell(invoiceLineCell);
            }

        }
        invoiceLinesTable.completeRow();

        return invoiceLinesTable;

    }


    private PdfPTable createInvoiceSummary() throws DocumentException {

        // Do zapłaty i słownie
        PdfPTable invoiceSummaryTable = new PdfPTable(2);
        invoiceSummaryTable.setWidths(new int[]{19, 100});
        invoiceSummaryTable.setWidthPercentage(100);
        invoiceSummaryTable.setSpacingBefore(70);
        PdfPCell invoiceAmountTextCell = new PdfPCell(new Paragraph("Do zapłaty:", getFont(FontFactory.TIMES_BOLD, 10)));
        if (!showBorder) invoiceAmountTextCell.setBorder(Rectangle.NO_BORDER);
        PdfPCell invoiceAmountCell = new PdfPCell(new Paragraph(NumberConverter.convertValueToString(getInvoice().getToPayValue().doubleValue(), false, " zł"), getFont(FontFactory.TIMES, 10)));
        if (!showBorder) invoiceAmountCell.setBorder(Rectangle.NO_BORDER);
        invoiceSummaryTable.addCell(invoiceAmountTextCell);
        invoiceSummaryTable.addCell(invoiceAmountCell);
        PdfPCell invoiceByWordTextCell = new PdfPCell(new Paragraph("Słownie:", getFont(FontFactory.TIMES_BOLD, 10)));

        PdfPCell invoiceByWordCell = new PdfPCell(new Paragraph(NumberConverter.valueToWords(getInvoice().getToPayValue().doubleValue()), getFont(FontFactory.TIMES, 10)));
        if (!showBorder) invoiceByWordTextCell.setBorder(Rectangle.NO_BORDER);
        if (!showBorder) invoiceByWordCell.setBorder(Rectangle.NO_BORDER);
        invoiceSummaryTable.addCell(invoiceByWordTextCell);
        invoiceSummaryTable.addCell(invoiceByWordCell);

        PdfPCell invoiceTransferTypeEmptyCell = invoiceSummaryTable.getDefaultCell();
        invoiceTransferTypeEmptyCell.addElement(new Paragraph(" ", getFont(FontFactory.TIMES, 8)));
        if (!showBorder) invoiceTransferTypeEmptyCell.setBorder(Rectangle.NO_BORDER);
        invoiceSummaryTable.addCell(invoiceTransferTypeEmptyCell);
        invoiceSummaryTable.addCell(invoiceTransferTypeEmptyCell);

        // Forma płatności
        PdfPCell invoiceTransferTypeTextCell = new PdfPCell(new Paragraph("Forma płatności:", getFont(FontFactory.TIMES, 9)));
        PdfPCell invoiceTransferTypeCell = new PdfPCell(new Paragraph(getInvoice().getPaymentType(), getFont(FontFactory.TIMES, 9)));
        if (!showBorder) invoiceTransferTypeTextCell.setBorder(Rectangle.NO_BORDER);
        if (!showBorder) invoiceTransferTypeCell.setBorder(Rectangle.NO_BORDER);
        invoiceSummaryTable.addCell(invoiceTransferTypeTextCell);
        invoiceSummaryTable.addCell(invoiceTransferTypeCell);

        // Nazwa banku
        PdfPCell invoiceBankNameTextCell = new PdfPCell(new Paragraph("Bank:", getFont(FontFactory.TIMES, 9)));
        PdfPCell invoiceBankNameCell = new PdfPCell(new Paragraph(getInvoice().getBankName(), getFont(FontFactory.TIMES, 9)));
        if (!showBorder) invoiceBankNameTextCell.setBorder(Rectangle.NO_BORDER);
        if (!showBorder) invoiceBankNameCell.setBorder(Rectangle.NO_BORDER);
        invoiceSummaryTable.addCell(invoiceBankNameTextCell);
        invoiceSummaryTable.addCell(invoiceBankNameCell);

        // Numer rachunku
        PdfPCell invoiceBankAccountNumberTextCell = new PdfPCell(new Paragraph("Numer konta:", getFont(FontFactory.TIMES, 9)));
        PdfPCell invoiceBankAccountNumberCell = new PdfPCell(new Paragraph(getInvoice().getBankAccountNumber(), getFont(FontFactory.TIMES, 9)));
        if (!showBorder) invoiceBankAccountNumberTextCell.setBorder(Rectangle.NO_BORDER);
        if (!showBorder) invoiceBankAccountNumberCell.setBorder(Rectangle.NO_BORDER);
        invoiceSummaryTable.addCell(invoiceBankAccountNumberTextCell);
        invoiceSummaryTable.addCell(invoiceBankAccountNumberCell);

        // Termin płatności
        PdfPCell invoicePaymentDateTextCell = new PdfPCell(new Paragraph("Termin płatności:", getFont(FontFactory.TIMES, 9)));
        PdfPCell invoicePaymentDateCell = new PdfPCell(new Paragraph(sdf.format(getInvoice().getPaymentDate()), getFont(FontFactory.TIMES, 9)));
        if (!showBorder) invoicePaymentDateTextCell.setBorder(Rectangle.NO_BORDER);
        if (!showBorder) invoicePaymentDateCell.setBorder(Rectangle.NO_BORDER);
        invoiceSummaryTable.addCell(invoicePaymentDateTextCell);
        invoiceSummaryTable.addCell(invoicePaymentDateCell);

        return invoiceSummaryTable;

    }


    private PdfPTable createSigns() throws DocumentException {

        PdfPTable signsTable = new PdfPTable(5);
        signsTable.setWidths(new int[]{15, 60, 100, 60, 15});
        signsTable.setWidthPercentage(100);
        signsTable.setSpacingBefore(150);

        PdfPCell signEmptyCell = signsTable.getDefaultCell();
        signEmptyCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell sellerSignCell = new PdfPCell(new Paragraph("Imię i nazwisko odbiorcy", getFont(FontFactory.TIMES, 8)));
        sellerSignCell.setBorder(Rectangle.TOP);
        //sellerSignCell.setBorderWidth(1);
        sellerSignCell.setBorderColor(BaseColor.GRAY);
        sellerSignCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell buyerSignCell = new PdfPCell(new Paragraph("Imię i nazwisko wystawcy", getFont(FontFactory.TIMES, 8)));
        buyerSignCell.setBorder(Rectangle.TOP);
        //buyerSignCell.setBorderWidth(1);
        buyerSignCell.setBorderColor(BaseColor.GRAY);
        buyerSignCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        signsTable.addCell(signEmptyCell);
        signsTable.addCell(sellerSignCell);
        signsTable.addCell(signEmptyCell);
        signsTable.addCell(buyerSignCell);
        signsTable.addCell(signEmptyCell);

        return signsTable;
    }

}
