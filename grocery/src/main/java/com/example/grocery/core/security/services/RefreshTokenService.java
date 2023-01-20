package com.example.grocery.core.security.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.grocery.business.constants.Messages.ErrorMessages;
import com.example.grocery.core.security.models.RefreshToken;
import com.example.grocery.core.security.repository.RefreshTokenRepository;
import com.example.grocery.core.security.repository.UserRepository;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.exceptions.TokenRefreshException;

import jakarta.transaction.Transactional;

@Service
public class RefreshTokenService {

    @Value("${grocery.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(
                userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND))); // orelsethrow
                                                                                                                       // yapılacak...
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new TokenRefreshException(refreshToken.getToken(),
                    "Refresh token was expired. Please new signin request");
        }
        return refreshToken;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public Long deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
}
