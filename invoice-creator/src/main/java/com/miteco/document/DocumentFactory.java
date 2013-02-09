package com.miteco.document;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DocumentFactory implements Producer {

    @Override
    public List<ByteArrayOutputStream> create(Object input) throws DocumentException, IOException {
        return null;
    }

    @Override
    public List<ByteArrayOutputStream> create(List<Object> input) throws DocumentException, IOException {
        List<ByteArrayOutputStream> result = new ArrayList<ByteArrayOutputStream>();
        for (Object object : input) {
            for (ByteArrayOutputStream baos : create(object)) {
                result.add(baos);
            }
        }
        return result;
    }

    protected Font getFont(String fontName, float size) {
        return FontFactory.getFont(fontName, BaseFont.CP1250, size);
    }

    protected Font getFont(String fontName, float size, BaseColor color) {
        Font font = getFont(fontName, size);
        font.setColor(color);
        return font;
    }



}
