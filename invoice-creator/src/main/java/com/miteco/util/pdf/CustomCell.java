package com.miteco.util.pdf;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;


public class CustomCell implements PdfPCellEvent{

    @Override
    public void cellLayout(PdfPCell pdfPCell, Rectangle rectangle, PdfContentByte[] pdfContentBytes) {
        PdfContentByte cb = pdfContentBytes[PdfPTable.LINECANVAS];
        cb.setLineDash(new float[] {1f, 1f}, 1);
        cb.stroke();
    }
}
