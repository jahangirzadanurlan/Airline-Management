package com.example.commonfilegenerator.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;

public class PdfGeneratorService {
    public static byte[] generatePdf(String content) {
        // PDF oluştur
        Document document = new Document();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter.getInstance(document, baos);
            document.open();
            // Başlık
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLUE);
            Paragraph title = new Paragraph("Ticket Information", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Boşluk
            document.add(Chunk.NEWLINE);

            // Metin
            Font contentFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);
            Paragraph para = new Paragraph(content, contentFont);
            para.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(para);

            // Boşluk
            document.add(Chunk.NEWLINE);

            // Resim
            Image image = Image.getInstance("C:\\Users\\cahan\\airline-management\\common-email\\src\\main\\resources\\travel.jpg");
            image.setAlignment(Element.ALIGN_CENTER);
            document.add(image);

            document.add(new Paragraph(content));
            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("generatePdfAndSendEmail not successfully!");
    }
}
