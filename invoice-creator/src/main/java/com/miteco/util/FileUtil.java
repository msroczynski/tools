package com.miteco.util;

import com.sun.org.apache.bcel.internal.generic.LSTORE;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: MS
 * Date: 01.09.12
 * Time: 00:54
 * To change this template use File | Settings | File Templates.
 */
public class FileUtil {

    public static void open(String path) {
        try {
            Desktop desktop = null;
            // Before more Desktop API is used, first check
            // whether the API is supported by this particular
            // virtual machine (VM) on this particular host.
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
                File file = new File(path);
                System.out.println("Opening " + file);
                desktop.open(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void open(java.util.List<String> paths) {
        try {
            Desktop desktop = null;
            // Before more Desktop API is used, first check
            // whether the API is supported by this particular
            // virtual machine (VM) on this particular host.
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
                for (String path : paths) {
                    File file = new File(path);
                    System.out.println("Opening " + file);
                    desktop.open(file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
