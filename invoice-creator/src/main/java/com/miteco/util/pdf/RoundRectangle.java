package com.miteco.util.pdf;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

public class RoundRectangle implements PdfPCellEvent {
    public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] canvas) {
        PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
        cb.setColorStroke(new GrayColor(0.8f));
        cb.roundRectangle(rect.getLeft() + 4, rect.getBottom(), rect.getWidth() - 8, rect.getHeight() - 4, 4);
        cb.stroke();
    }
}