package dev.forum.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {
    String username;
    String password;
}
