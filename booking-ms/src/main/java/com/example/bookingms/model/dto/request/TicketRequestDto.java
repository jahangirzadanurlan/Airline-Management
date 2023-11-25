package com.example.bookingms.model.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketRequestDto {
    String firstName;
    String lastName;
    String email;
}
