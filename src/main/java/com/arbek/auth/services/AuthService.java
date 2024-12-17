package com.arbek.auth.services;

import com.arbek.auth.entities.LocalUser;
import com.arbek.auth.entities.UserType;
import com.arbek.auth.repository.UserRepository;
import com.arbek.auth.utils.AuthResponse;
import com.arbek.auth.utils.LoginRequest;
import com.arbek.auth.utils.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final RefreshTokenService refreshTokenService;
  private final AuthenticationManager authenticationManager;

  public AuthResponse register(RegisterRequest registerRequest) {
    var localUser = LocalUser.builder()
        .username(registerRequest.getUsername())
        .firstName(registerRequest.getFirstName())
        .lastName(registerRequest.getLastName())
        .password(passwordEncoder.encode(registerRequest.getPassword()))
        .userType(UserType.USER)
        .build();

    LocalUser savedUser = userRepository.save(localUser);
    var accessToken = jwtService.generateToken(savedUser);
    var refreshToken = refreshTokenService.createRefreshToken(savedUser.getUsername());

    return AuthResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken.getRefreshToken())
        .build();
  }

  public AuthResponse login(LoginRequest loginRequest) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(),
            loginRequest.getPassword()
        )
    );

    var localUser = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    var accessToken = jwtService.generateToken(localUser);
    var refreshToken = refreshTokenService.createRefreshToken(loginRequest.getUsername());

    return AuthResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken.getRefreshToken())
        .build();
  }
}
