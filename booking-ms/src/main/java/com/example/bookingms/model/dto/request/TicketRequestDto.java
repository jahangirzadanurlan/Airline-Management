package com.example.bookingms.model.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketRequestDto {
    @NotBlank(message = "First Name cannot be blank")
    String firstName;

    @NotBlank(message = "Last Name cannot be blank")
    String lastName;

    @Email(message = "Email is wrong")
    String email;

    @Positive(message = "Seat number should be a positive number")
    int planeSeatNumber;
}
