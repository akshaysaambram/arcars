package com.arcars.arcars.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arcars.arcars.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    Boolean existsByEmail(String email);
}
