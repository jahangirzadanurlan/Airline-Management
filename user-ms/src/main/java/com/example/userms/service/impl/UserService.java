package com.example.userms.service.impl;

import com.example.userms.model.dto.response.UserRequestDto;
import com.example.userms.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Override
    public String saveUser(UserRequestDto userRequestDto) {
        return null;
    }

    @Override
    public String authenticateUser(String username, String password) {
        return null;
    }

    @Override
    public String renewPassword(String token) {
        return null;
    }

    @Override
    public String resetsPassword(String newPassword, String repeatPassword) {
        return null;
    }

    @Override
    public String refreshToken(String token, Long id) {
        return null;
    }

    @Override
    public String confirmation(String token) {
        return null;
    }

    @Override
    public String adminRegistration(UserRequestDto userRequestDto, String token, Long id) {
        return null;
    }
}
