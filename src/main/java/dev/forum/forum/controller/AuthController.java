package dev.forum.forum.controller;

import dev.forum.forum.dto.AuthenticationResponse;
import dev.forum.forum.dto.LoginRequest;
import dev.forum.forum.dto.RefreshTokenRequest;
import dev.forum.forum.dto.RegisterRequest;
import dev.forum.forum.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    @ResponseStatus(CREATED)
    public void createNewUserAccount(@RequestBody RegisterRequest registerRequest) {
        authService.signup(registerRequest);
    }

    @GetMapping("/confirmation/{token}")
    public String verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return "Account activated successfully";
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public String logout(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        authService.logout(refreshTokenRequest);
        return "Logged out successfully";
    }
}
