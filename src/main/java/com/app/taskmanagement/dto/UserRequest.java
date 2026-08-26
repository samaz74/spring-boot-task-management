package com.app.taskmanagement.dto;

import com.app.taskmanagement.model.enums.Roles;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRequest {
    private String fName;
    private String lName;
    private String email;
    private String password;
    private Roles role;
}
