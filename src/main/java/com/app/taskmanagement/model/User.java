package com.app.taskmanagement.model;

import com.app.taskmanagement.model.enums.Roles;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fName;
    private String lName;
    @Column(unique = true)
    private String email;
    private String password;
    private Roles role;
    @CreationTimestamp
    private LocalDateTime created_at;
    public User(String fName, String lName, String email, String password, Roles role) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.password = password;
        this.role = role;

    }

}
