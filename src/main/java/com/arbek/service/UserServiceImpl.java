package com.arbek.service;

import com.arbek.auth.entities.LocalUser;
import com.arbek.auth.repository.UserRepository;
import com.arbek.dto.UpdateUserRequest;
import com.arbek.dto.UserDto;
import com.arbek.auth.services.JwtService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public UserServiceImpl(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }


    @Override
    public UserDto updateUser(String token, UpdateUserRequest updateUserRequest) {
        String currentUsername = jwtService.extractUsername(token);

        LocalUser currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + currentUsername));

        String newUsername = updateUserRequest.getUsername();
        if (newUsername != null && !newUsername.equals(currentUsername)) {
            if (userRepository.findByUsername(newUsername).isPresent()) {
                throw new RuntimeException("Username already exists");
            }
            currentUser.setUsername(newUsername);
        }

        if (updateUserRequest.getFirstName() != null) {
            currentUser.setFirstName(updateUserRequest.getFirstName());
        }
        if (updateUserRequest.getLastName() != null) {
            currentUser.setLastName(updateUserRequest.getLastName());
        }

        LocalUser updatedUser = userRepository.save(currentUser);

        String newToken = jwtService.generateToken(updatedUser);

        return mapToDto(updatedUser, newToken);
    }


    @Override
    public UserDto getCurrentUser(String token) {
        String username = jwtService.extractUsername(token);
        LocalUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if (!jwtService.isTokenValid(token, user)) {
            throw new RuntimeException("Invalid token");
        }

        return mapToDto(user, token);
    }

    private UserDto mapToDto(LocalUser user, String token) {
        return new UserDto(
                user.getUserId(),
                user.getUsername(),
                user.getUserType(),
                user.getFirstName(),
                user.getLastName(),
                token
        );
    }

}
