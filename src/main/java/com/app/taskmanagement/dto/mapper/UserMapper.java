package com.app.taskmanagement.dto.mapper;

import com.app.taskmanagement.dto.UserRequest;
import com.app.taskmanagement.dto.UserResponse;
import com.app.taskmanagement.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(UserRequest userRequest) {
        return new User (
                userRequest.getFName()
                ,userRequest.getLName()
                ,userRequest.getEmail()
                ,userRequest.getPassword()
                ,userRequest.getRole());
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fName(user.getFName())
                .lName(user.getLName())
                .email(user.getEmail())
                .role(user.getRole())
                .created_at(user.getCreated_at())
                .build();
    }
}
