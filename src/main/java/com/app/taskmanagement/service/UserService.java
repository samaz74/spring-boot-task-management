package com.app.taskmanagement.service;

import com.app.taskmanagement.exception.ResourceNotFoundException;
import com.app.taskmanagement.model.User;
import com.app.taskmanagement.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByIdEntity(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("کاربر یافت نشد."));
    }
    public User getUserByEmailEntity(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(()-> new ResourceNotFoundException("کاربر یافت نشد."));
    }

}
