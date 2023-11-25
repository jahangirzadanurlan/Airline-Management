package com.example.notificationms.kafka;

import com.example.commonemail.service.MailSenderService;
import com.example.commonnotification.dto.request.KafkaRequest;
import com.example.notificationms.service.PdfGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class KafkaListeners {
    private final MailSenderService mailSenderService;
    private final PdfGeneratorService pdfGeneratorService;

    @KafkaListener(topics = "confirm-topic",groupId = "groupId")
    void UserConfirmationListener(KafkaRequest request) {
        log.info("confirm-topic Consumer ise dusdu -> {}",request.getUsername());
        mailSenderService.sendConfirmationMail(request);
    }

    @KafkaListener(topics = "set-psw-topic",groupId = "groupId")
    void AdminConfirmationListener(KafkaRequest request) {
        log.info("set-psw-topic Consumer ise dusdu -> {}",request.getUsername());
        mailSenderService.sendAdminConfirmationMail(request);
    }

    @KafkaListener(topics = "email-topic",groupId = "groupId")
    void passwordResetMailSendingListener(KafkaRequest request) {
        log.info("email-topic Consumer ise dusdu -> {}",request.getEmail());

        String setPasswordLink = "http://localhost:8081/user/auth/reset-password/page";
        String emailContent = "<html><body><p>Please click the following link to reset password for your account:</p>"
                + "<a href=\"" + setPasswordLink + "\">Confirm Account</a>"
                + "</body></html>";

        mailSenderService.sendMail("Reset Password!",emailContent, request.getEmail());
    }

    @KafkaListener(topics = "otp-topic",groupId = "groupId")
    void otpMailSendingListener(KafkaRequest request) {
        String otp = request.getToken();
        log.info("otp-topic Consumer ise dusdu -> {}",request.getEmail());

        String emailContent = "<html><body><p>Please " + otp + " </p>"
                + "</body></html>";

        mailSenderService.sendMail("Renew Password!",emailContent, request.getEmail());
    }

    @KafkaListener(topics = "ticket-topic",groupId = "groupId")
    void ticketPDFMailSendingListener(KafkaRequest request) {
        log.info("ticket-topic Consumer ise dusdu -> {}",request.getEmail());
        pdfGeneratorService.generatePdfAndSendEmail("Ticket Buying Mail!",request.toString(),request.getEmail(),"ticket.pdf");
    }
}

