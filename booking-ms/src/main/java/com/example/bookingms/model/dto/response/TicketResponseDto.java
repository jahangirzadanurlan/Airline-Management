package com.example.bookingms.model.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketResponseDto {
    Long id;
    String firstName;
    String lastName;
    String fromAirline;
    String toAirline;
    LocalDateTime departureDateTime;
    LocalDateTime arrivalDateTime;
    double price;
    Long flightId;
}
