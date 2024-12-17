package com.arbek.service;

import com.arbek.dto.UpdateUserRequest;
import com.arbek.dto.UserDto;

public interface UserService {

    UserDto updateUser(String token, UpdateUserRequest updateUserRequest);

    UserDto getCurrentUser(String token);


}
