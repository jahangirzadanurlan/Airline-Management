package com.example.flightms.model.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlightResponseDto {
    Long id;
    Long airplaneId;
    Long fromAirlineId;
    Long toAirlineId;
    LocalDateTime departureDateTime;
    LocalDateTime arrivalDateTime;
    double price;
}
