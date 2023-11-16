package com.example.userms.service.impl;

import com.example.commonsecurity.auth.services.JwtService;
import com.example.commonsecurity.model.RoleType;
import com.example.userms.model.dto.request.AuthenticationRequest;
import com.example.userms.model.dto.response.AuthenticationResponse;
import com.example.userms.model.dto.response.UserRequestDto;
import com.example.userms.model.entity.Role;
import com.example.userms.model.entity.User;
import com.example.userms.model.enums.Constant;
import com.example.userms.repository.RoleRepository;
import com.example.userms.repository.UserRepository;
import com.example.userms.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public String saveUser(UserRequestDto request) {
        Role role = roleRepository.findRoleByName(RoleType.USER)
                .orElseThrow(() -> new RuntimeException("Role not found!"));
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .paspPin(request.getPaspPin())
                .paspSeria(request.getPaspSeria())
                .phoneNumber(request.getPhoneNumber())
                .birthdate(request.getBirthdate())
                .role(role)
                .build();
        userRepository.save(user);

        confirmation(UUID.randomUUID().toString());
        return Constant.SAVE_IS_SUCCESSFULLY.getMessage();
    }

    @Override
    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        Optional<User> user = userRepository.findUserByUsernameOrEmail(request.getUsername());
        if (!user.get().isEnabled())      {
            throw new RuntimeException("Account not active!");
        }//

        String accessToken=jwtService.generateToken(user.orElseThrow());
        String refreshToken=jwtService.generateRefreshToken(user.orElseThrow());

        return AuthenticationResponse.builder()
                .message(user.orElseThrow().getEmail() + " login is successfully")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
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
        //Emaile link gonder
        return null;
    }

    @Override
    public String adminRegistration(UserRequestDto userRequestDto, String token, Long id) {
        return null;
    }
}
