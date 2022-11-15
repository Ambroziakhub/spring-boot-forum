package dev.forum.forum.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private String username;
}
