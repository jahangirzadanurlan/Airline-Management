package com.example.flightms.model.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Future;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlightRequestDto {
    @Positive(message = "From airline id should be a positive number")
    Long fromAirlineId;

    @Positive(message = "To airline id should be a positive number")
    Long toAirlineId;

    @Future(message = "Departure date and time should be in the future")
    LocalDateTime departureDateTime;

    @Future(message = "Arrival date and time should be in the future")
    LocalDateTime arrivalDateTime;

    @PositiveOrZero(message = "Initial price should be a positive or zero number")
    double initialPrice;

    @Positive(message = "Airplane id should be a positive number")
    Long airplaneId;
}
