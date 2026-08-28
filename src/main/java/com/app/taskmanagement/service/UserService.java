package com.app.taskmanagement.service;

import com.app.taskmanagement.dto.UserRequest;
import com.app.taskmanagement.dto.UserResponse;
import com.app.taskmanagement.dto.mapper.UserMapper;
import com.app.taskmanagement.exception.ResourceNotFoundException;
import com.app.taskmanagement.model.User;
import com.app.taskmanagement.model.enums.Roles;
import com.app.taskmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public User getUserByIdEntity(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }
    public User getUserByEmailEntity(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }

    public UserResponse grtUserById(Long id){
        return userRepository.findById(id).map(userMapper::toResponse).orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }
    public UserResponse getUserByEmail(String email) {
        return userRepository.findUserByEmail(email).map(userMapper::toResponse).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toResponse).collect(Collectors.toList());
    }
    public List<UserResponse> getUsersByEmailContaining(String email) {
        return userRepository.findUserByEmailContaining(email).stream().map(userMapper::toResponse).collect(Collectors.toList());
    }

    public List<UserResponse> getUsersByFNameContaining(String fName) {
        return userRepository.findUserByFNameContains(fName).stream().map(userMapper::toResponse).collect(Collectors.toList());
    }
    public List<UserResponse> grtUsersByLNameContaining(String lName) {
        return userRepository.findUserByLNameContains(lName).stream().map(userMapper::toResponse).collect(Collectors.toList());
    }
    public List<UserResponse> getUsersByRole(Roles role) {
        return userRepository.findUserByRole(role).stream().map(userMapper::toResponse).collect(Collectors.toList());
    }
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);
        user.setId(id);
        userRepository.save(user);
        return userMapper.toResponse(user);
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
