package com.example.airplanems.model.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlaneRequestDto {
    String name;
    int masSeats;
    int maxSpeed;
}


