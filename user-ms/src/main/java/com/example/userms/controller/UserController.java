package com.example.userms.controller;

import com.example.userms.model.dto.request.AdminNewPasswordRequestDto;
import com.example.userms.model.dto.request.AuthenticationRequest;
import com.example.userms.model.dto.response.AuthenticationResponse;
import com.example.userms.model.dto.request.UserRequestDto;
import com.example.userms.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @PostMapping("/auth/registration")
    public ResponseEntity<String> registration(@RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userRequestDto));
    }

    @PostMapping("/auth/authentication")
    public ResponseEntity<AuthenticationResponse> authentication(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok().body(userService.authenticateUser(request));
    }

    @GetMapping("/auth/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestHeader(name = "Authorization") String token,@RequestHeader(name = "UserId") Long id){
        return ResponseEntity.ok().body(userService.refreshToken(token,id));
    }//Authorization problemi

    @GetMapping("/auth/confirmation/{token}")
    public ResponseEntity<String> confirmation(@PathVariable String token){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.confirmAccount(token));
    }

    @GetMapping("/auth/set-password/{token}")
    public ResponseEntity<String> setPasswordPage(@PathVariable String token){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.passwordSetPage(token));
    }

    @PostMapping("/auth/set-password")
    public ResponseEntity<String> setPasswordForAdmin(@RequestBody AdminNewPasswordRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.setPasswordForAdminAccount(requestDto));
    }

    @PostMapping("/admin/registration")
    public ResponseEntity<String> adminRegistration(@RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveAdmin(userRequestDto));
    }







}
