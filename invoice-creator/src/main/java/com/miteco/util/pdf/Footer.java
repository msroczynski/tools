package com.miteco.util.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.IOException;
import java.util.Date;

public class Footer extends PdfPageEventHelper {

    private PdfContentByte cb;

    private BaseFont bf = null;

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        try {
            bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cb = writer.getDirectContent();
    }

    @Override
    public void onEndPage (PdfWriter writer, Document document) {
        super.onEndPage(writer, document);
        Rectangle rect = document.getPageSize();
        /*
        cb.beginText();
        cb.setFontAndSize(bf, 8);
        //cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Outbox Sp. z o.o., ul. Grójecka 5, 02-019 Warszawa,/nXII Wydział Gospodarczy Krajowego Rejestru Sądowego pod KRS 0000213301,/nNIP 527-244-71-95, kapitał zakładowy 270 000 złotych", pageSize.getRight(40), pageSize.getBottom(30), 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER,"", recPage.getLeft(), recPage.getBottom(), 0);
        cb.endText();
          */


        final Paragraph phrase = new Paragraph("some long, multi-line string goes here");
        ColumnText ct = new ColumnText(writer.getDirectContent());
        ct.addText(new Phrase("fff"));
        //ct.addText(phrase);
        //ct.addElement(phrase);
        //ct.setAlignment(Element.ALIGN_CENTER);
        ct.setSimpleColumn(rect.getLeft(), rect.getBottom(), rect.getRight(), rect.getBottom());
        //System.out.println("onEndPage");
        try {
            ct.go();
        } catch (DocumentException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }
}