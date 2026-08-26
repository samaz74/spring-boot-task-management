package com.app.taskmanagement.dto;

import com.app.taskmanagement.model.enums.Roles;
import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    private Long id;
    private String fName;
    private String lName;
    private String email;
    private Roles role;
    private LocalDateTime created_at;
}
