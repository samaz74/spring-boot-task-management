package com.app.taskmanagement.dto;

import com.app.taskmanagement.model.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AuthResponse {
    private String email;
    private Roles role;
    private String token;

}
