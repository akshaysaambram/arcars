package com.arcars.arcars.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arcars.arcars.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
