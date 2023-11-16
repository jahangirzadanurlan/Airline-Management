package com.example.userms.service;

import com.example.userms.model.dto.request.AuthenticationRequest;
import com.example.userms.model.dto.response.AuthenticationResponse;
import com.example.userms.model.dto.response.UserRequestDto;

public interface IUserService {
    String saveUser(UserRequestDto userRequestDto);
    AuthenticationResponse authenticateUser(AuthenticationRequest request);
    String renewPassword(String token);
    String resetsPassword(String newPassword,String repeatPassword);
    String refreshToken(String token,Long id);
    String confirmation(String token);
    String adminRegistration(UserRequestDto userRequestDto,String token,Long id);
}

