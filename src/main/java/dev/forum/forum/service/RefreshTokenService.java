package dev.forum.forum.service;

import dev.forum.forum.exception.ForumException;
import dev.forum.forum.model.RefreshToken;
import dev.forum.forum.model.User;
import dev.forum.forum.repository.RefreshTokenRepo;
import dev.forum.forum.repository.UserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepo refreshTokenRepo;
    private final UserRepo userRepo;
    @Value("${refresh-token.expiration}")
    private final Long refreshTokenDurationMs;

    public RefreshTokenService(RefreshTokenRepo refreshTokenRepo,
                               UserRepo userRepo,
                               @Value("${refresh-token.expiration}") Long refreshTokenDurationMs) {
        this.refreshTokenRepo = refreshTokenRepo;
        this.userRepo = userRepo;
        this.refreshTokenDurationMs = refreshTokenDurationMs;
    }

    public RefreshToken generateRefreshToken(String username) {
        // find user
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ForumException("User " + username + " not found."));

        // create refresh token
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(this.refreshTokenDurationMs));

        // save token to repository
        return refreshTokenRepo.save(refreshToken);
    }

    public RefreshToken getRefreshToken(String token) {
        return refreshTokenRepo.findByToken(token)
                .orElseThrow(() -> new ForumException("Invalid refresh Token"));
    }

    public void verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepo.deleteByToken(refreshToken.getToken());
            throw new ForumException("Refresh token is expired.");
        }
    }

    public RefreshToken updateRefreshToken(RefreshToken refreshToken) {
        // create new RefreshToken
        RefreshToken newRefreshToken = new RefreshToken(
                refreshToken.getId(),
                UUID.randomUUID().toString(),
                refreshToken.getUser(),
                Instant.now().plusMillis(this.refreshTokenDurationMs)
        );
        // update token in the database
        refreshTokenRepo.updateRefreshTokenById(
                newRefreshToken.getId(),
                newRefreshToken.getToken(),
                newRefreshToken.getUser(),
                newRefreshToken.getExpiryDate()
        );
        return newRefreshToken;
    }
}