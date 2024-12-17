package com.arbek.controllers;

import com.arbek.dto.UpdateUserRequest;
import com.arbek.dto.UserDto;
import com.arbek.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUserHandler(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        UserDto currentUser = userService.getCurrentUser(token);

        return ResponseEntity.ok(currentUser);
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateUserHandler(@RequestHeader("Authorization") String authorizationHeader,
                                                     @RequestBody UpdateUserRequest updateUserRequest) {
        String token = authorizationHeader.replace("Bearer ", "");

        UserDto updatedUser = userService.updateUser(token, updateUserRequest);

        return ResponseEntity.ok(updatedUser);
    }
}
