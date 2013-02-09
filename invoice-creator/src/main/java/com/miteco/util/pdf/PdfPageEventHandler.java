package com.miteco.util.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PdfPageEventHandler extends PdfPageEventHelper {

    PdfContentByte cb;

    BaseFont bf = null;

    Date dNow = null;

    public static final SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private String footerText;

    public String getFooterText() {
        return footerText;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        dNow = new Date();
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
    public void onCloseDocument(PdfWriter writer, Document document) {
        super.onCloseDocument(writer, document);
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        super.onStartPage(writer, document);
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        super.onEndPage(writer, document);
        Rectangle pageSize = document.getPageSize();
        cb.beginText();
        cb.setFontAndSize(bf, 8);
        cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, (getFooterText()==null?"":getFooterText()) + ft.format(dNow), pageSize.getRight(40), pageSize.getBottom(30), 0);
        cb.endText();
    }
}
