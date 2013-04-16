package com.miteco.document;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.miteco.document.xml.model.*;
import com.miteco.util.NumberConverter;
import com.miteco.util.pdf.Footer;
import com.miteco.util.pdf.PdfDocument;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AcceptanceProtocolFactory extends DocumentFactory{


    static Logger log = Logger.getLogger(AcceptanceProtocolFactory.class.getName());

    public AcceptanceProtocol getAcceptanceProtocol() {
        return acceptanceProtocol;
    }

    public void setAcceptanceProtocol(AcceptanceProtocol acceptanceProtocol) {
        this.acceptanceProtocol = acceptanceProtocol;
    }

    private AcceptanceProtocol acceptanceProtocol;

    private boolean showBorder;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private void addContent(Document document) throws IOException, DocumentException {
        document.add(createLogo());
        document.add(createHeader());
        document.add(createPartyInfo());
        document.add(createProtocolInfo());
        document.add(createSigns());
    }

    @Override
    public java.util.List<ByteArrayOutputStream> create(Object object) throws DocumentException, IOException {

        java.util.List<ByteArrayOutputStream> result = new ArrayList<ByteArrayOutputStream>();

        setAcceptanceProtocol((AcceptanceProtocol)object);

        // Create instance of PDF document
        PdfDocument document = new PdfDocument();
        document.setMargins(50, 50, 50, 50);

        // Create in memory
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        Footer event = new Footer();
        writer.setPageEvent(event);

        document.open();
        addContent(document);

        result.add(baos);
        document.close();

        return result;
    }

    private PdfPTable createLogo() throws DocumentException, IOException {
        PdfPTable logoTable = new PdfPTable(2);
        logoTable.setWidthPercentage(100);
        logoTable.setWidths(new float[]{10, 3});
        Image logoImage = Image.getInstance("img/logo-outbox.png");
        PdfPCell logoCell = new PdfPCell();
        logoCell.setHorizontalAlignment(Rectangle.RIGHT);
        logoCell.setImage(logoImage);
        logoImage.scalePercent(15);
        if (!showBorder) logoCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell emptyCell = logoTable.getDefaultCell();
        emptyCell.setBorder(Rectangle.NO_BORDER);
        logoTable.addCell(emptyCell);
        logoTable.addCell(logoCell);
        return logoTable;
    }

    private PdfPTable createHeader() throws DocumentException, IOException {


        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        headerTable.setSpacingBefore(40);

        PdfPCell protocolTitleCell = new PdfPCell(new Paragraph("PROTOKÓŁ ODBIORU USŁUG", getFont(FontFactory.TIMES_BOLD, 16)));
        protocolTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        if (!showBorder) protocolTitleCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(protocolTitleCell);

        PdfPCell protocolDateInfoCell = new PdfPCell(new Paragraph(String.format("z dnia %s",sdf.format(getAcceptanceProtocol().getIssuingDate())), getFont(FontFactory.TIMES, 12)));
        protocolDateInfoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        if (!showBorder) protocolDateInfoCell.setBorder(Rectangle.NO_BORDER);
        protocolDateInfoCell.setPaddingTop(20);
        headerTable.addCell(protocolDateInfoCell);

        return headerTable;

    }

    private PdfPTable createPartyInfo() throws DocumentException {

        PdfPTable partyInfoTable = new PdfPTable(2);
        partyInfoTable.setSpacingBefore(70);
        partyInfoTable.setWidthPercentage(100);
        partyInfoTable.setWidths(new int[]{10, 10});

        PdfPCell ordererHeaderCell = new PdfPCell(new Paragraph("Zamawiający", getFont(FontFactory.TIMES, 10)));
        ordererHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        ordererHeaderCell.setVerticalAlignment(Element.ALIGN_CENTER);
        Paragraph ordererParagraph = null;
        if (getAcceptanceProtocol().hasOrderer()) {
            Orderer orderer = getAcceptanceProtocol().getOrderer();
            ordererParagraph = new Paragraph(orderer.getName(), getFont(FontFactory.TIMES_BOLD, 10));
            ordererParagraph.add(Chunk.NEWLINE);
            if (orderer.hasAddress()) {
                ordererParagraph.add(String.format("%s %s", orderer.getAddress().getPostalCode(), orderer.getAddress().getCity()));
                ordererParagraph.add(Chunk.NEWLINE);
                ordererParagraph.add(String.format("%s %s", orderer.getAddress().getStreet(), orderer.getAddress().getBuildingNumber()));
            }

        }
        PdfPCell ordererCell = new PdfPCell(ordererParagraph);
        ordererCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        ordererCell.setVerticalAlignment(Element.ALIGN_CENTER);

        PdfPCell recipientHeaderCell = new PdfPCell(new Paragraph("Przyjmujący zamówienie", getFont(FontFactory.TIMES, 10)));
        recipientHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        recipientHeaderCell.setVerticalAlignment(Element.ALIGN_MIDDLE);


        Paragraph recipientParagraph = null;
        if (getAcceptanceProtocol().hasRecipient()) {
            Recipient recipient = getAcceptanceProtocol().getRecipient();
            recipientParagraph = new Paragraph(50, recipient.getName(), getFont(FontFactory.TIMES_BOLD, 10));
            recipientParagraph.add(Chunk.NEWLINE);
            if (recipient.hasAddress()) {
                recipientParagraph.add(String.format("%s %s", recipient.getAddress().getPostalCode(), recipient.getAddress().getCity()));
                recipientParagraph.add(Chunk.NEWLINE);
                recipientParagraph.add(String.format("%s %s", recipient.getAddress().getStreet(), recipient.getAddress().getBuildingNumber()));
            }
        }

        PdfPCell recipientCell = new PdfPCell(recipientParagraph);
        recipientCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        recipientCell.setVerticalAlignment(Element.ALIGN_CENTER);

        partyInfoTable.addCell(ordererHeaderCell);
        partyInfoTable.addCell(recipientHeaderCell);
        partyInfoTable.addCell(ordererCell);
        partyInfoTable.addCell(recipientCell);

        return partyInfoTable;

    }


    private PdfPTable createProtocolInfo() throws DocumentException {

        PdfPTable protocolInfoTable = new PdfPTable(1);
        protocolInfoTable.setWidthPercentage(100);
        protocolInfoTable.setSpacingBefore(100);

        Paragraph protocolParagraph = new Paragraph(String.format("Potwierdzamy wykonanie pracy w wymiarze %s MD w ramach realizacji zamówienia %s z dnia %s.",NumberConverter.convertValueToString(getAcceptanceProtocol().getDaysNumber(), true) ,getAcceptanceProtocol().getOrderNumber(),sdf.format(getAcceptanceProtocol().getOrderDate())), getFont(FontFactory.TIMES, 11));
        protocolParagraph.add(Chunk.NEWLINE);
        protocolParagraph.add(Chunk.NEWLINE);
        protocolParagraph.add("Niniejszy protokół upoważnia do wystawienia faktury z uwzględnieniem pracochłonności oraz ewentualnych kosztów poniesionych w związku z realizacją zamówienia.");

        PdfPCell protocolInfoCell = new PdfPCell(protocolParagraph);
        protocolInfoCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        protocolInfoCell.setBorder(Rectangle.NO_BORDER);
        protocolInfoTable.addCell(protocolInfoCell);

        return protocolInfoTable;
    }


    private PdfPTable createSigns() throws DocumentException {

        PdfPTable signsTable = new PdfPTable(5);
        signsTable.setWidths(new int[]{15, 60, 100, 60, 15});
        signsTable.setWidthPercentage(100);
        signsTable.setSpacingBefore(170);

        PdfPCell signEmptyCell = signsTable.getDefaultCell();
        signEmptyCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell sellerSignCell = new PdfPCell(new Paragraph("Outbox Sp. z o.o.", getFont(FontFactory.TIMES, 10)));
        sellerSignCell.setBorder(Rectangle.TOP);
        //sellerSignCell.setBorderWidth(1);
        sellerSignCell.setBorderColor(BaseColor.GRAY);
        sellerSignCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell buyerSignCell = new PdfPCell(new Paragraph("Zleceniobiorca", getFont(FontFactory.TIMES, 10)));
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
