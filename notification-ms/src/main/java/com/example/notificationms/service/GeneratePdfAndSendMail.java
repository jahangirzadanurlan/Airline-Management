package com.example.notificationms.service;

import com.example.commonemail.service.MailSenderService;
import com.example.commonfilegenerator.service.PdfGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeneratePdfAndSendMail {
    private final MailSenderService mailSenderService;

    public void generatePdfAndSendEmail(String subject,String content,String email, String filePath) {
        byte[] baos = PdfGeneratorService.generatePdf(content);
        String body = email + " your ticket information!";

        try {
            mailSenderService.sendEmailWithAttachment(email, subject, body, baos, filePath);
        } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
