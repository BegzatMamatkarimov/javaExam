package com.arbek.controllers;

import com.arbek.auth.entities.LocalUser;
import com.arbek.auth.entities.RefreshToken;
import com.arbek.auth.services.AuthService;
import com.arbek.auth.services.JwtService;
import com.arbek.auth.services.RefreshTokenService;
import com.arbek.auth.utils.AuthResponse;
import com.arbek.auth.utils.LoginRequest;
import com.arbek.auth.utils.RefreshTokenRequest;
import com.arbek.auth.utils.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/")
@CrossOrigin(origins = "*")
public class AuthController {
  private final AuthService authService;
  private final RefreshTokenService refreshTokenService;
  private final JwtService jwtService;

  public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtService jwtService) {
    this.authService = authService;
    this.refreshTokenService = refreshTokenService;
    this.jwtService = jwtService;
  }

  @PostMapping("/signup")
  public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
    return ResponseEntity.ok(authService.register(registerRequest));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok(authService.login(loginRequest));
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
    RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
    LocalUser localUser = refreshToken.getLocalUser();

    String accessToken = jwtService.generateToken(localUser);

    return ResponseEntity.ok(AuthResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken.getRefreshToken())
        .build());
  }
}

