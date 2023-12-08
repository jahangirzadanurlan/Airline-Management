package com.example.userms.model.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequestDto {
    @NotBlank(message = "Username cannot be blank")
    String username;

    @Size(min = 6, message = "Password should be at least 6 characters long")
    @Pattern(regexp = ".*\\d.*", message = "Password should contain at least 1 digit")
    String password;

    @Size(min = 6, message = "Phone number should be at least 6 characters long")
    @NotBlank(message = "Phone number cannot be blank")
    String phoneNumber;

    @Email(message = "Email is not in a correct format")
    String email;

    LocalDate birthdate;

    @NotBlank(message = "Passport PIN cannot be blank")
    @Size(min = 6, max = 10, message = "Passport PIN should be between 6 and 10 characters long")
    String paspPin;

    @NotBlank(message = "Passport seria cannot be blank")
    @Size(min = 2, max = 2, message = "Passport seria should be 2 characters long")
    String paspSeria;
}
