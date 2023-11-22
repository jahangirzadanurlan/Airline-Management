package com.example.userms.service;

import com.example.userms.model.dto.request.AdminNewPasswordRequestDto;
import com.example.userms.model.dto.request.AuthenticationRequest;
import com.example.userms.model.dto.response.AuthenticationResponse;
import com.example.userms.model.dto.request.UserRequestDto;
import com.example.userms.model.entity.User;

public interface IUserService {
    String saveUser(UserRequestDto userRequestDto);
    String saveAdmin(UserRequestDto userRequestDto);
    AuthenticationResponse authenticateUser(AuthenticationRequest request);
    String passwordSetPage(String token);
    String setPasswordForAdminAccount(AdminNewPasswordRequestDto requestDto);
    String renewPassword(String token);
    String resetsPassword(String newPassword,String repeatPassword);
    AuthenticationResponse refreshToken(String token,Long id);
    void sendConfirmationLink(String token, User user);
    String confirmAccount(String token);
}

