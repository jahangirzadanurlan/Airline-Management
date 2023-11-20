package com.example.notificationms.kafka;

import com.example.commonemail.service.MailSenderService;
import com.example.commonnotification.dto.request.ConfirmationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
public class KafkaListeners {
    private final MailSenderService mailSenderService;
    @KafkaListener(topics = "confirm-topic",groupId = "groupId")
    void UserConfirmationListener(ConfirmationRequest request) {
        mailSenderService.sendMail(request);
    }
}
