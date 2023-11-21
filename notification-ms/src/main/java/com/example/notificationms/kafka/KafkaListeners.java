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
        log.info("Consumer ise dusdu");
        mailSenderService.sendConfirmationMail(request);
    }
}

