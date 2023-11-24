package com.example.flightms.model.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlightRequestDto {
    Long fromAirlineId;
    Long toAirlineId;
    LocalDateTime departureDateTime;
    LocalDateTime arrivalDateTime;
    double initialPrice;
    Long airplaneId;
}
