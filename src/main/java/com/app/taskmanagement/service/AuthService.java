package com.app.taskmanagement.service;


import com.app.taskmanagement.dto.AuthResponse;
import com.app.taskmanagement.dto.UserRequest;
import com.app.taskmanagement.dto.mapper.UserMapper;
import com.app.taskmanagement.exception.DuplicateResourceException;
import com.app.taskmanagement.exception.ResourceNotFoundException;
import com.app.taskmanagement.model.InvalidatedToken;
import com.app.taskmanagement.model.User;
import com.app.taskmanagement.repository.InvalidatedTokenRepository;
import com.app.taskmanagement.repository.UserRepository;
import com.app.taskmanagement.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil, InvalidatedTokenRepository invalidatedTokenRepository, UserMapper userMapper){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.invalidatedTokenRepository = invalidatedTokenRepository;
        this.userMapper = userMapper;
    }
    public AuthResponse createUser(UserRequest userRequest) {
        if (userRepository.existsUserByEmail(userRequest.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }else{
            userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            userRepository.save(userMapper.toEntity(userRequest));
            return new AuthResponse(userRequest.getEmail(),userRequest.getRole(),null);
        }
    }

    public AuthResponse loginUser(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        User user = userRepository.findUserByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        String token = jwtUtil.generateToken(user);
        return new AuthResponse(user.getEmail(),user.getRole(),token);
    }

    public void logoutUser(String token) {

        String email = jwtUtil.extractEmail(token);
        LocalDateTime expiration = jwtUtil.extractExpiration(token);
        InvalidatedToken invalidatedToken = new InvalidatedToken(token,email,expiration);
        invalidatedTokenRepository.save(invalidatedToken);

    }



}
