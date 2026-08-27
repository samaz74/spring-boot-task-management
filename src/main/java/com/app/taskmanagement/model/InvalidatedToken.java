package com.app.taskmanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class InvalidatedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private String email;
    private LocalDateTime expiresAt;
    @CreationTimestamp
    private LocalDateTime createdAt;

    public InvalidatedToken(String token, String email, LocalDateTime expiresAt) {
        this.token = token;
        this.email = email;
        this.expiresAt = expiresAt;
    }
}
