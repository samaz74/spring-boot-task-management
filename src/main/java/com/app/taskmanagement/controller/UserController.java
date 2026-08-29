package com.app.taskmanagement.controller;

import com.app.taskmanagement.dto.UserRequest;
import com.app.taskmanagement.dto.UserResponse;
import com.app.taskmanagement.model.enums.Roles;
import com.app.taskmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/userId/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUserById(@PathVariable Long userId) {
        return userService.grtUserById(userId);
    }
    @GetMapping("/email/{userEmail}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUserByEmail(@PathVariable String userEmail) {
        return userService.getUserByEmail(userEmail);
    }
    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        return userService.getAllUsers();
    }
    @GetMapping("/search/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsersByEmail(@PathVariable String email) {
        return userService.getUsersByEmailContaining(email);
    }
    @GetMapping("/search/firstName/{firstName}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsersByFirstName(@PathVariable String firstName) {
        return userService.getUsersByFNameContaining(firstName);
    }
    @GetMapping("/search/lastName/{lastName}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsersByLastName(@PathVariable String lastName) {
        return userService.grtUsersByLNameContaining(lastName);
    }
    @GetMapping("/search/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsersByRole(@PathVariable Roles role) {
        return userService.getUsersByRole(role);
    }
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(@PathVariable Long userId,@Valid @RequestBody UserRequest userRequest) {
        return userService.updateUser(userId, userRequest);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);

    }

}
