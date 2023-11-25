package com.example.notificationms.service;

import com.example.commonemail.service.MailSenderService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class PdfGeneratorService {
    private final MailSenderService mailSenderService;

    public void generatePdfAndSendEmail(String subject,String content,String email, String filePath) {
        // PDF oluştur
        Document document = new Document();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter.getInstance(document, baos);
            document.open();
            document.add(new Paragraph(content));
            document.close();

            // PDF dosyasını e-posta ile gönder
            mailSenderService.sendEmailWithAttachment(email, subject, content, baos.toByteArray(), filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
