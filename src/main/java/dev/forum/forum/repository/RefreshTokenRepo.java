package dev.forum.forum.repository;

import dev.forum.forum.model.RefreshToken;
import dev.forum.forum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query("UPDATE RefreshToken t SET t.id = ?1, t.token = ?2, t.user = ?3, t.expiryDate = ?4 WHERE t.id = ?1")
    void updateRefreshTokenById(Long id, String token, User user, Instant expiryDate);

    void deleteByToken(String token);
}
