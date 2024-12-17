package com.arbek.dto;

import com.arbek.auth.entities.UserType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
  private Integer userId;

  @NotBlank(message = "User's name couldn't be empty!")
  private String username;

  private UserType userType;

  private String firstName;

  private String lastName;

  private String token;

}
