package dev.forum.forum.service;

import dev.forum.forum.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;

    @Value("${jwt.expiration}")
    private final int jwtExpirationInMillis;

    public TokenService(JwtEncoder jwtEncoder, @Value("${jwt.expiration}") int jwtExpirationInMillis) {
        this.jwtEncoder = jwtEncoder;
        this.jwtExpirationInMillis = jwtExpirationInMillis;
    }

    private String buildToken(String username, String scope) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusMillis(jwtExpirationInMillis))
                .subject(username)
                .claim("scope", scope)
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateToken(Authentication authentication) {
        return buildToken(authentication.getName(), authentication.getAuthorities().toString());
    }

    public String generateToken(User user) {
        return buildToken(user.getEmail(), user.getUserRole().toString());
    }

}
