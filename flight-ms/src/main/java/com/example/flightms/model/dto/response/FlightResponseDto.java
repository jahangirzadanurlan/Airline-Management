package com.example.flightms.model.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlightResponseDto {
    Long fromAirlineId;
    Long toAirlineId;
    LocalDateTime departureDateTime;
    LocalDateTime arrivalDateTime;
    double initialPrice;
}
