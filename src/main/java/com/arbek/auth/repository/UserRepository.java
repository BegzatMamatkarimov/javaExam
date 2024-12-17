package com.arbek.auth.repository;

import com.arbek.auth.entities.LocalUser;
import com.arbek.dto.UpdateUserRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<LocalUser, Integer> {
  Optional<LocalUser> findByUsername(String username);
  }
