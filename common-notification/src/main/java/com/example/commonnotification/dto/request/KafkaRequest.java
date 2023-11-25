package com.example.commonnotification.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaRequest {
    String username;
    String email;
    String token;

    String firstName;
    String lastName;
    Long fromAirlineId;//Id meselesi
    Long toAirlineId;
    LocalDateTime departureDateTime;
    LocalDateTime arrivalDateTime;
    double price;
    Long flightId;
}

