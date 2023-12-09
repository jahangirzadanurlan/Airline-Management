package com.example.airplanems.model.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Builder
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlaneRequestDto {
    @NotBlank(message = "Name cannot be blank")
    String name;

    @Positive(message = "Seats should be a positive number")
    int masSeats;

    @Positive(message = "Speed should be a positive number")
    int maxSpeed;
}


