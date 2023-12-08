package com.example.userms.service;

import com.example.userms.model.dto.request.AdminNewPasswordRequestDto;
import com.example.userms.model.dto.request.AuthenticationRequest;
import com.example.userms.model.dto.request.PasswordRequestDto;
import com.example.userms.model.dto.response.AuthenticationResponse;
import com.example.userms.model.dto.request.UserRequestDto;
import com.example.userms.model.entity.User;
import org.springframework.http.ResponseEntity;

public interface IUserService {
    String saveUser(UserRequestDto userRequestDto);
    String saveAdmin(UserRequestDto userRequestDto);
    AuthenticationResponse authenticateUser(AuthenticationRequest request);
    String passwordSetPage(String token);
    String setPasswordForAdminAccount(AdminNewPasswordRequestDto requestDto);
    ResponseEntity<String> sendOTP(String token);
    ResponseEntity<String> checkOtp(String username,String otp);
    ResponseEntity<String> checkEmailInDatabase(String email);
    ResponseEntity<String> resetsPassword(String token, PasswordRequestDto passwordRequestDto);
    AuthenticationResponse refreshToken(String token,Long id);
    void sendConfirmationLink(String token, User user,String topicName);
    String confirmAccount(String token);
}

