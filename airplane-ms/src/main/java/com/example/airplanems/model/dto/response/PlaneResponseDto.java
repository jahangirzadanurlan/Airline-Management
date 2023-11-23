package com.example.airplanems.model.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlaneResponseDto {
    String name;
    int masSeats;
    int maxSpeed;
}


