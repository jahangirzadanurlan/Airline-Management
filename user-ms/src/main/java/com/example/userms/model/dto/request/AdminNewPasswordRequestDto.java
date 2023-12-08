package com.example.userms.model.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminNewPasswordRequestDto {
    @NotBlank(message = "Token cannot be blank")
    String token;

    @Size(min = 6, message = "Password should be at least 6 characters long")
    @Pattern(regexp = ".*\\d.*", message = "Password should contain at least 1 digit")
    String newPassword;
    String repeatPassword;
}
