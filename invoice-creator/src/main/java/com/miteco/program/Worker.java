package com.miteco.program;


import com.itextpdf.text.DocumentException;
import com.miteco.document.Processor;

import java.io.IOException;

public class Worker {

    public static void main(String[] args) throws IOException, DocumentException {
        new Processor().run();
    }

}
