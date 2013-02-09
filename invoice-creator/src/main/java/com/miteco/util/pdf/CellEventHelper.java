package com.miteco.util.pdf;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;


public class CellEventHelper {


   public class DottedLine implements PdfPCellEvent {

       public void cellLayout(PdfPCell cell, Rectangle position,
                               PdfContentByte[] canvases) {

            PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
            cb.saveState();
            cb.setLineCap(PdfContentByte.LINE_CAP_ROUND);
            //cb.setLineDash(0, 5, 1);
            cb.setLineDash(0, 2, 0);
            //cb.rectangle(position);
            cb.rectangle(position.getLeft(),position.getBottom(), position.getWidth(), position.getHeight());
            //cb.moveTo(position.getLeft(), position.getBottom());
            //cb.lineTo(position.getRight(), position.getBottom());
            //cb.lineTo(position.getRight(), position.getTop());
            //cb.lineTo(position.getLeft(), position.getTop());
            //cb.lineTo(position.getLeft(), position.getBottom());
            cb.stroke();
            cb.restoreState();

        }
    }

    public class DottedLineNoLeft implements PdfPCellEvent {

       public void cellLayout(PdfPCell cell, Rectangle position,
                               PdfContentByte[] canvases) {

            PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
            cb.saveState();
            cb.setLineCap(PdfContentByte.LINE_CAP_ROUND);
            cb.setLineDash(0, 2, 0);
            //cb.rectangle(position.getLeft(),position.getBottom(), position.getWidth(), position.getHeight());
            cb.moveTo(position.getLeft(), position.getBottom());
            cb.lineTo(position.getRight(), position.getBottom());
            cb.lineTo(position.getRight(), position.getTop());
            cb.lineTo(position.getLeft(), position.getTop());
            //cb.lineTo(position.getLeft(), position.getBottom());
            cb.stroke();
            cb.restoreState();

        }
    }





    class SimpleLine implements PdfPCellEvent {
        public void cellLayout(PdfPCell cell, Rectangle position,
                               PdfContentByte[] canvas) {
            // TODO Auto-generated method stub
            PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
            cb.saveState();
            cb.setLineWidth(0.5f);
            cb.moveTo(position.getLeft(), position.getBottom());
            cb.lineTo(position.getRight(), position.getBottom());
            cb.stroke();
            cb.restoreState();
        }

    }


}
