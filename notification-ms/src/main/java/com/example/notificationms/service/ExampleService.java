package com.example.notificationms.service;

import com.example.commonnotification.dto.request.ConfirmationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExampleService {
    private final KafkaTemplate<String, ConfirmationRequest> kafkaTemplate;

}
