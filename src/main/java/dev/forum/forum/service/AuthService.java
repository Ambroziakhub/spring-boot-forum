package dev.forum.forum.service;

import dev.forum.forum.dto.AuthenticationResponse;
import dev.forum.forum.dto.LoginRequest;
import dev.forum.forum.dto.RefreshTokenRequest;
import dev.forum.forum.dto.RegisterRequest;
import dev.forum.forum.email.EmailDetails;
import dev.forum.forum.email.EmailService;
import dev.forum.forum.exception.ForumException;
import dev.forum.forum.model.ConfirmationToken;
import dev.forum.forum.model.RefreshToken;
import dev.forum.forum.model.user.SecurityUser;
import dev.forum.forum.model.user.User;
import dev.forum.forum.model.user.UserRole;
import dev.forum.forum.repository.ConfirmationTokenRepo;
import dev.forum.forum.repository.UserRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenRepo confirmationTokenRepo;
    private final EmailService emailService;

    public void signup(RegisterRequest registerRequest) {
        User user = new User();

        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setUserRole(UserRole.USER);

        // User is disabled until he confirms his email address
        user.setEnabled(Boolean.FALSE);
        userRepo.save(user);

        // create confirmation token
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                null, token, user, Instant.now().plus(1, ChronoUnit.DAYS)
        );
        // save to database
        confirmationTokenRepo.save(confirmationToken);

        // send conformation mail with token
        emailService.sendSimpleMail(
                new EmailDetails(
                        user.getEmail(),
                        "Hello " + user.getUsername() + "!\n" +
                                "Please click on the below url to activate your account : " +
                                "http://localhost:8080/api/auth/confirmation/" + token,
                        "Verify your email address",
                        null
                )
        );
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        log.info("Authenticating user with username {}", loginRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        String username = securityUser.getUsername();
        String accessToken = tokenService.generateToken(securityUser.user());
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(username);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .username(username)
                .build();
    }

    public void logout(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteByToken(refreshTokenRequest.getRefreshToken());
    }

    public void verifyAccount(String token) {

        ConfirmationToken confirmationToken =
                confirmationTokenRepo.findByToken(token)
                        .orElseThrow(() -> new ForumException("Token: " + token + " not found."));

        String username = confirmationToken.getUser().getUsername();

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found."));

        // if found set enabled
        user.setEnabled(Boolean.TRUE);
        userRepo.save(user);
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        // found refresh token in the database
        RefreshToken refreshToken = refreshTokenService.getRefreshToken(refreshTokenRequest.getRefreshToken());

        // check if not expired
        refreshTokenService.verifyExpiration(refreshToken);
        // update refresh token
        String newRefreshToken = refreshTokenService.updateRefreshToken(refreshToken).getToken();
        // generate new access token
        String accessToken = tokenService.generateToken(refreshToken.getUser());

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .username(refreshToken.getUser().getUsername())
                .build();
    }
}




