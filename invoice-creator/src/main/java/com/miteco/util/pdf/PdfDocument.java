package com.miteco.util.pdf;

import com.itextpdf.text.*;

import java.io.IOException;


public class PdfDocument extends Document {

    public PdfDocument() {
        super.setPageSize(PageSize.A4);
    }

    public PdfDocument(Rectangle pageSize) {
        super.setPageSize(pageSize);
    }

    public void addMetadata(String title, String subject, String keywords, String author, String creator)
    {
        super.addTitle(title);
        super.addSubject(subject);
        super.addKeywords(keywords);
        super.addAuthor(author);
        super.addCreator(creator);
    }


    public Image getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(Image headerImage) {
        this.headerImage = headerImage;
    }

    private Image headerImage;

    public void addLogo() {

        try {

            this.setHeaderImage(Image.getInstance("resources/img/logo.jpg"));

            if (getHeaderImage() != null) {

            if (getHeaderImage().getScaledWidth() > 100 || getHeaderImage().getScaledHeight() > 100) {
                getHeaderImage().scaleToFit(100, 100);
            }
                getHeaderImage().setAlignment(Image.ALIGN_LEFT);
                super.add(getHeaderImage());
            }
            else System.out.println("Cannot load logo image");

        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }





}
