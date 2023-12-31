package com.example.userms.service.impl;

import com.example.commonexception.enums.ExceptionsEnum;
import com.example.commonexception.exceptions.GeneralException;
import com.example.commonnotification.dto.request.KafkaRequest;
import com.example.commonsecurity.auth.SecurityHelper;
import com.example.commonsecurity.auth.services.JwtService;
import com.example.commonsecurity.model.RoleType;
import com.example.userms.model.dto.request.AdminNewPasswordRequestDto;
import com.example.userms.model.dto.request.AuthenticationRequest;
import com.example.userms.model.dto.request.PasswordRequestDto;
import com.example.userms.model.dto.response.AuthenticationResponse;
import com.example.userms.model.dto.request.UserRequestDto;
import com.example.userms.model.entity.ConfirmationToken;
import com.example.userms.model.entity.Role;
import com.example.userms.model.entity.User;
import com.example.userms.model.enums.Constant;
import com.example.userms.repository.ConfirmationTokenRepository;
import com.example.userms.repository.RoleRepository;
import com.example.userms.repository.UserRepository;
import com.example.userms.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService,IUserService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SecurityHelper securityHelper;
    private final KafkaTemplate<String, KafkaRequest> kafkaTemplate;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsernameOrEmail(username);

        return new org.springframework.security.core.userdetails.User(
                user.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username))
                        .getUsername(),
                user.get().getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(user.get().getRole().getName().name())));
    }

    @Override
    public String saveUser(UserRequestDto request) {
        Role role = roleRepository.findRoleByName(RoleType.USER)
                .orElseThrow(() -> new GeneralException(ExceptionsEnum.ROLE_NOT_FOUND));
        saveUserToDatabase(request, role,"confirm-topic");
        log.info("{} -> user created",request.getUsername());
        return Constant.SAVE_IS_SUCCESSFULLY.getMessage();
    }

    @Override
    public String saveAdmin(UserRequestDto request) {
        Role role = roleRepository.findRoleByName(RoleType.ADMIN)
                .orElseThrow(() -> new GeneralException(ExceptionsEnum.ROLE_NOT_FOUND));
        saveUserToDatabase(request, role,"set-psw-topic");
        log.info("{} -> admin created",request.getUsername());
        return Constant.SAVE_IS_SUCCESSFULLY.getMessage();
    }

    private void saveUserToDatabase(UserRequestDto request, Role role,String topicName) {
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

        sendConfirmationLink(UUID.randomUUID().toString(),user,topicName);
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

        String accessToken=jwtService.generateToken(user.orElseThrow(() -> new GeneralException(ExceptionsEnum.USER_NOT_FOUND)));
        String refreshToken=jwtService.generateRefreshToken(user.orElseThrow());

        log.info("{} -> user loging",request.getUsername());
        return AuthenticationResponse.builder()
                .message(user.orElseThrow().getEmail() + " login is successfully")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    //-----Renew Password------
    @Override
    public ResponseEntity<String> sendOTP(String token) {
        securityHelper.authHeaderIsValid(token);
        String jwt = token.substring(7);
        if (!jwtService.isTokenExpired(jwt)) {
            String username = jwtService.extractUsername(jwt);
            Optional<User> user = userRepository.findUserByUsernameOrEmail(username);
            String otp = createAndSaveOtp(user);

            KafkaRequest kafkaRequest = KafkaRequest.builder()
                    .email(user.orElseThrow(() -> new GeneralException(ExceptionsEnum.Token_NOT_FOUND)).getEmail())
                    .token(otp)
                    .build();
            kafkaTemplate.send("otp-topic",kafkaRequest);
            return ResponseEntity.ok().body("We are send OTP to " + user.get().getEmail());
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expired!");
        }
    }

    private String createAndSaveOtp(Optional<User> user) {
        Random random = new Random();
        String otp = String.valueOf(random.nextInt(9000) + 1000);

        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(otp)
                .user(user.orElseThrow(() -> new GeneralException(ExceptionsEnum.USER_NOT_FOUND)))
                .createdAt(LocalDateTime.now())
                .build();
        confirmationTokenRepository.save(confirmationToken);
        return otp;
    }

    @Override
    public ResponseEntity<String> checkOtp(String username,String otp) {
        Optional<User> user = userRepository.findUserByUsernameOrEmail(username);
        if (user.isEmpty()){
            throw new GeneralException(ExceptionsEnum.USERNAME_NOT_FOUND);
        }

        Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findConfirmationTokenByToken(otp);
        if (confirmationToken.isPresent()){
            return ResponseEntity.ok().body("Otp is true");
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Otp is wrong");
        }
    }

    //----------------------------

    @Override
    public ResponseEntity<String> resetsPassword(String token, PasswordRequestDto passwordRequestDto) {
        if (passwordRequestDto.getNewPassword().equals(passwordRequestDto.getRepeatPassword())){
            extractUserAndSetPassword(token, passwordRequestDto);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Password changing is successfully!");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Password changing is not successfully!");
        }
    }

    private void extractUserAndSetPassword(String token, PasswordRequestDto passwordRequestDto) {
        securityHelper.authHeaderIsValid(token);
        String jwt = token.substring(7);
        if (!jwtService.isTokenExpired(jwt)){
            String username = jwtService.extractUsername(jwt);
            Optional<User> user = userRepository.findUserByUsernameOrEmail(username);
            user.orElseThrow(() -> new GeneralException(ExceptionsEnum.USER_NOT_FOUND))
                    .setPassword(passwordEncoder.encode(passwordRequestDto.getNewPassword()));
            userRepository.save(user.get());
        }
    }

    @Override
    public ResponseEntity<String> checkEmailInDatabase(String email){
        log.info("email -> {}",email);
        Optional<User> user = userRepository.findUserByUsernameOrEmail(email);
        if (user.isPresent()){
            KafkaRequest kafkaRequest = KafkaRequest.builder()
                    .email(user.get().getEmail())
                    .build();
            kafkaTemplate.send("email-topic",kafkaRequest);
            return ResponseEntity.ok().body("We send link to your email for password changing");
        }else {
            throw new GeneralException(ExceptionsEnum.EMAIL_NOT_REGISTERED);
        }
    }

    @Override
    public AuthenticationResponse refreshToken(String token, Long id) {
        if (!securityHelper.authHeaderIsValid(token)){
            throw new GeneralException(ExceptionsEnum.TOKEN_IS_WRONG);
        }

        String jwt = token.substring(7);
        String username = jwtService.extractUsername(jwt);
        Optional<User> userById = userRepository.findUserById(id);

        if (username != null){
            if (userById.orElseThrow(() -> new GeneralException(ExceptionsEnum.USER_NOT_FOUND))
                    .getUsername().equals(username)){
                if(jwtService.isTokenValid(jwt,userById.orElseThrow())){
                    String accessToken=jwtService.generateToken(userById.get());
                    String refreshToken=jwtService.generateRefreshToken(userById.get());

                    log.info("{} token refreshing is successfully",username);
                    return AuthenticationResponse.builder()
                            .message("Refreshing is successfully!")
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                }
            }else {
                throw new GeneralException(ExceptionsEnum.USER_ID_IS_WRONG);
            }
        }else {
            throw new GeneralException(ExceptionsEnum.USERNAME_IS_NULL);
        }
        return null;
    }

    @Override
    public void sendConfirmationLink(String token,User user,String topicName) {
        KafkaRequest request = KafkaRequest.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .token(token)
                .build();
        saveConfirmationToken(token, user);
        kafkaTemplate.send(topicName,request);
    }

    private void saveConfirmationToken(String token, User user) {
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .token(token)
                .build();
        confirmationTokenRepository.save(confirmationToken);
    }

    @Override
    public String confirmAccount(String token) {
        enableUserAccount(token);
        return "Your account has been activated!";
    }

    @Override
    public String setPasswordForAdminAccount(AdminNewPasswordRequestDto requestDto){
        if (requestDto.getNewPassword().equals(requestDto.getRepeatPassword())){
            enableAdminAccount(requestDto.getToken(),requestDto.getNewPassword());
            return "Your account has been activated!";
        }else {
            throw new GeneralException(ExceptionsEnum.PASSWORDS_IS_NOT_EQUALS);
        }
    }

    private void enableUserAccount(String token) {
        Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findConfirmationTokenByToken(token);
        Optional<User> user = userRepository.findUserByUsernameOrEmail(confirmationToken.orElseThrow(() -> new RuntimeException("Token not found!"))
                                                                                .getUser().getUsername());
        user.orElseThrow(() -> new GeneralException(ExceptionsEnum.USER_NOT_FOUND))
                .setEnabled(true);
        confirmationToken.get().setConfirmedAt(LocalDateTime.now());

        confirmationTokenRepository.save(confirmationToken.get());
        userRepository.save(user.get());
    }

    private void enableAdminAccount(String token,String password) {
        Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findConfirmationTokenByToken(token);
        Optional<User> user = userRepository.findUserByUsernameOrEmail(confirmationToken.orElseThrow(() -> new RuntimeException("Token not found!"))
                                                                            .getUser().getUsername());
        user.orElseThrow(() -> new GeneralException(ExceptionsEnum.USER_NOT_FOUND))
                .setEnabled(true);
        user.get().setPassword(passwordEncoder.encode(password));
        confirmationToken.get().setConfirmedAt(LocalDateTime.now());

        confirmationTokenRepository.save(confirmationToken.get());
        userRepository.save(user.get());
    }

    @Override
    public String passwordSetPage(String token) {
        Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findConfirmationTokenByToken(token);
        Optional<User> user = userRepository.findUserByUsernameOrEmail(confirmationToken.orElseThrow(() -> new RuntimeException("Token not found!"))
                                                                            .getUser().getUsername());
        user.orElseThrow(() -> new GeneralException(ExceptionsEnum.USER_NOT_FOUND))
                .setEnabled(true);

        return user.get().getUsername() + " plese send POST request to '/auth/set-password' link!";
    }
}
