package com.example.flightms.model.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlaneResponseDto {
    Long id;
    String name;
    int masSeats;
    int availableSeats;
    int maxSpeed;
    boolean isBusy;

}


