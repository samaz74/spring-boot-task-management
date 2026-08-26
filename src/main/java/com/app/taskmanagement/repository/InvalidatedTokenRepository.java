package com.app.taskmanagement.repository;

import com.app.taskmanagement.model.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken,Long> {

    Optional<InvalidatedToken> findInvalidatedTokenByToken(String token);

    Optional<InvalidatedToken> findInvalidatedTokenByEmail(String email);

    List<InvalidatedToken> findInvalidatedTokenByCreatedAtAfter(LocalDateTime createdAtAfter);

    void deleteByExpiresAtBefore(LocalDateTime dateTime);

    boolean existsByToken(String token);
}
