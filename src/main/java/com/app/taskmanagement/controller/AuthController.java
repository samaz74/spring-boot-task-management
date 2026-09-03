package com.app.taskmanagement.controller;

import com.app.taskmanagement.annotation.TrackExecutionTime;
import com.app.taskmanagement.dto.AuthResponse;
import com.app.taskmanagement.dto.LoginRequest;
import com.app.taskmanagement.dto.UserRequest;
import com.app.taskmanagement.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/createUser")
    @PreAuthorize("hasRole('ADMIN')")
    public AuthResponse createUser(@Valid @RequestBody UserRequest userRequest) {
        return authService.createUser(userRequest);
    }

    @PostMapping("/login")
    @TrackExecutionTime(threshold = 500)
    public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String authHeader) {
        authService.logoutUser(authHeader);
    }
}
