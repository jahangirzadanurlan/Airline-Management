package com.example.userms.service.impl;

import com.example.commonnotification.dto.request.ConfirmationRequest;
import com.example.commonsecurity.auth.SecurityHelper;
import com.example.commonsecurity.auth.services.JwtService;
import com.example.commonsecurity.model.RoleType;
import com.example.userms.model.dto.request.AuthenticationRequest;
import com.example.userms.model.dto.response.AuthenticationResponse;
import com.example.userms.model.dto.request.UserRequestDto;
import com.example.userms.model.entity.Role;
import com.example.userms.model.entity.User;
import com.example.userms.model.enums.Constant;
import com.example.userms.repository.RoleRepository;
import com.example.userms.repository.UserRepository;
import com.example.userms.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService,IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SecurityHelper securityHelper;
    private final KafkaTemplate<String, ConfirmationRequest> kafkaTemplate;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsernameOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().getName().name())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.isEnabled())
                .build();
    }

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

        sendConfirmationLink(UUID.randomUUID().toString(),user);
        log.info("{} -> user created",request.getUsername());
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
            log.error("Authentication failed: {}", e.getMessage());//Userin aktiv olub olmamağını haradan bilir?
            throw new RuntimeException(e);
        }

        Optional<User> user = userRepository.findUserByUsernameOrEmail(request.getUsername());

        String accessToken=jwtService.generateToken(user.orElseThrow());
        String refreshToken=jwtService.generateRefreshToken(user.orElseThrow());

        log.info("{} -> user loging",request.getUsername());
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
    public AuthenticationResponse refreshToken(String token, Long id) {
        if (!securityHelper.authHeaderIsValid(token)){
            throw new RuntimeException("Token is wrong!");//
        }

        String jwt = token.substring(7);
        String username = jwtService.extractUsername(jwt);
        Optional<User> userById = userRepository.findUserById(id);

        if (username != null){
            if (userById.orElseThrow(() -> new RuntimeException(id + " id User not found!"))
                    .getUsername().equals(username)){
                if(jwtService.isTokenValid(jwt,userById.orElseThrow())){
                    String accessToken=jwtService.generateToken(userById.get());
                    String refreshToken=jwtService.generateRefreshToken(userById.get());

                    log.info("{} token refreshing is successfully",username);
                    return AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                }
            }
        }
        throw new RuntimeException("Token is wrong!");
    }

    @Override
    public void sendConfirmationLink(String token,User user) {
        ConfirmationRequest request = ConfirmationRequest.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .token(token)
                .build();
        kafkaTemplate.send("confirm-topic",request);
    }

    @Override
    public String confirmAccount(String token) {
        return null;
    }


    @Override
    public String adminRegistration(UserRequestDto userRequestDto, String token, Long id) {
        return null;
    }
}
