package com.arcars.arcars.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arcars.arcars.model.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByEmailOfTokenGeneratedUserAndTokenType(String emailOfTokenGeneratedUser, String tokenType);

}
