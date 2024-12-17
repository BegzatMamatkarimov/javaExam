package com.arbek.auth.services;

import com.arbek.auth.entities.LocalUser;
import com.arbek.auth.entities.RefreshToken;
import com.arbek.auth.repository.RefreshTokenRepository;
import com.arbek.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
  private final UserRepository userRepository;

  private final RefreshTokenRepository refreshTokenRepository;

  public RefreshTokenService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
    this.userRepository = userRepository;
    this.refreshTokenRepository = refreshTokenRepository;
  }

  public RefreshToken createRefreshToken(String username) {
    LocalUser localUser = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

    RefreshToken refreshToken = localUser.getRefreshToken();

    if (refreshToken == null) {
      long refreshTokenValidity = 2 * 24 * 60 * 60 * 1000;

      refreshToken = RefreshToken.builder()
          .refreshToken(UUID.randomUUID().toString())
          .expirationTime(Instant.now().plusMillis(refreshTokenValidity))
          .localUser(localUser)
          .build();

      refreshTokenRepository.save(refreshToken);
    }

    return refreshToken;
  }

  public RefreshToken verifyRefreshToken(String refreshToken) {
    RefreshToken refToken = refreshTokenRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new RuntimeException("Refresh token not found!"));

    if (refToken.getExpirationTime().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(refToken);
      throw new RuntimeException("Refresh token expired");
    }

    return refToken;
  }
}
