package com.miteco.util.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.IOException;


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

        try {

            Rectangle page = document.getPageSize();

            PdfPTable footerTable = getFooterSignatures();
            footerTable.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin()-250);
            footerTable.writeSelectedRows(0, -1, (page.getWidth()-footerTable.getTotalWidth())/2, document.bottomMargin() + 10, writer.getDirectContent());

        } catch (DocumentException ex) {

            ex.printStackTrace();

        }

    }

    private PdfPTable getFooterSignatures() throws DocumentException {

        Font fontStyleFooters = FontFactory.getFont(FontFactory.TIMES_ROMAN, BaseFont.CP1250, 7, Font.NORMAL);
        PdfPTable footerTable = new PdfPTable(1);
        footerTable.setWidthPercentage(100);
        footerTable.getDefaultCell().setPadding(2);
        footerTable.getDefaultCell().setBorderWidth(0);
        footerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        Paragraph para = new Paragraph();
        para.add(new Chunk("  Outbox Sp. z o.o., ul. Grójecka 5, 02-019 Warszawa,", fontStyleFooters));
        para.add(new Chunk("\n  XII Wydział Gospodarczy Krajowego Rejestru Sądowego pod KRS 0000213301,", fontStyleFooters));
        para.add(new Chunk("\n  NIP 527-244-71-95, kapitał zakładowy 270 000 złotych", fontStyleFooters));
        footerTable.addCell(para);
        return footerTable;
    }


}