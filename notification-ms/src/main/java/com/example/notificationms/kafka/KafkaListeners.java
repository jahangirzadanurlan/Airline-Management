package com.example.notificationms.kafka;

import com.example.commonemail.service.MailSenderService;
import com.example.commonnotification.dto.request.ConfirmationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class KafkaListeners {
    private final MailSenderService mailSenderService;
    @KafkaListener(topics = "confirm-topic",groupId = "groupId")
    void UserConfirmationListener(ConfirmationRequest request) {
        log.info("confirm-topic Consumer ise dusdu -> {}",request.getUsername());
        mailSenderService.sendConfirmationMail(request);
    }

    @KafkaListener(topics = "set-psw-topic",groupId = "groupId")
    void AdminConfirmationListener(ConfirmationRequest request) {
        log.info("set-psw-topic Consumer ise dusdu -> {}",request.getUsername());
        mailSenderService.sendAdminConfirmationMail(request);
    }
}

