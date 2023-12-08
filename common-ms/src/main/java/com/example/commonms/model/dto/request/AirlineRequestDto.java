package com.example.commonms.model.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirlineRequestDto {
    @NotBlank(message = "Country cannot be blank")
    String country;

    @NotBlank(message = "Airline name cannot be blank")
    String airlineName;

}
