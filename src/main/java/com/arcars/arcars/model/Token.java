package com.arcars.arcars.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usernameOfTokenGeneratedUser;

    private String tokenType;

    private String token;

    @CreationTimestamp
    private LocalDateTime generatedAt;

    public Token(String usernameOfTokenGeneratedUser, String tokenType, String token) {
        this.usernameOfTokenGeneratedUser = usernameOfTokenGeneratedUser;
        this.tokenType = tokenType;
        this.token = token;
    }

}
