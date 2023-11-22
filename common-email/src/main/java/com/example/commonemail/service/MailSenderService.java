package com.example.commonemail.service;

import com.example.commonnotification.dto.request.ConfirmationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RequiredArgsConstructor
public class MailSenderService {
    private final JavaMailSender javaMailSender;

    public void sendConfirmationMail(ConfirmationRequest request) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        try {
            helper.setTo(request.getEmail());
            helper.setSubject("Confirm account!");
            String confirmationLink = "http://localhost:8081/user/auth/confirmation/" + request.getToken();
            String emailContent = "<html><body><p>Please click the following link to confirm your account:</p>"
                    + "<a href=\"" + confirmationLink + "\">Confirm Account</a>"
                    + "</body></html>";

            helper.setText(emailContent, true);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(message);
    }

    public void sendAdminConfirmationMail(ConfirmationRequest request) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        try {
            helper.setTo(request.getEmail());
            helper.setSubject("Set password!");
            String setPasswordLink = "http://localhost:8081/user/auth/set-password/" + request.getToken();
            String emailContent = "<html><body><p>Please click the following link to set password for your account:</p>"
                    + "<a href=\"" + setPasswordLink + "\">Confirm Account</a>"
                    + "</body></html>";

            helper.setText(emailContent, true);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(message);
    }
}
