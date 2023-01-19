package com.example.grocery.core.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.example.grocery.core.security.models.RefreshToken;
import com.example.grocery.core.security.models.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    Long deleteByUser(User user);
}
