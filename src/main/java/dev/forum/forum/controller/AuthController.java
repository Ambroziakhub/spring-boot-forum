package dev.forum.forum.controller;

import dev.forum.forum.dto.*;
import dev.forum.forum.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(authService.login(loginRequest), OK);
    }

    @PostMapping("/register")
    public void createNewUserAccount(@RequestBody RegisterRequest registerRequest) {
        authService.signup(registerRequest);
    }

    @GetMapping("/confirmation/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account activated successfully", OK);
    }

    @GetMapping("/refresh/token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        return new ResponseEntity<>(authService.refreshToken(refreshTokenRequest), OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody @Valid  RefreshTokenRequest refreshTokenRequest) {
        authService.logout(refreshTokenRequest);
        return ResponseEntity.status(OK).body("Logged out successfully");
    }
}
