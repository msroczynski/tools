package com.miteco.document;

import com.itextpdf.text.DocumentException;
import com.miteco.document.xml.model.Invoice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public interface Producer {

    public abstract List<ByteArrayOutputStream> create(Object input) throws DocumentException, IOException;

    public abstract List<ByteArrayOutputStream> create(List<Object> input) throws DocumentException, IOException;

}
