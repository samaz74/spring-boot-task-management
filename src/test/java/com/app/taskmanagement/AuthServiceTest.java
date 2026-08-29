package com.app.taskmanagement;

import com.app.taskmanagement.dto.AuthResponse;
import com.app.taskmanagement.dto.LoginRequest;
import com.app.taskmanagement.dto.UserRequest;
import com.app.taskmanagement.dto.mapper.UserMapper;
import com.app.taskmanagement.exception.ResourceNotFoundException;
import com.app.taskmanagement.model.User;
import com.app.taskmanagement.model.enums.Roles;
import com.app.taskmanagement.repository.InvalidatedTokenRepository;
import com.app.taskmanagement.repository.UserRepository;
import com.app.taskmanagement.security.JwtUtil;
import com.app.taskmanagement.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.mockito.ArgumentMatchers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private InvalidatedTokenRepository invalidatedTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;


    @Test
    void createUserWithRightData() {
        UserRequest userRequest = new UserRequest("Pey","Az","AZ@in.ir","12345678", Roles.USER);
        User user = new User("Pey","Az","AZ@in.ir","12345678", Roles.USER);
        when(userMapper.toEntity(userRequest)).thenReturn(new User("Pey","Az","AZ@in.ir","12345678", Roles.USER));
        when(userRepository.save(any(User.class))).thenReturn(user);

        AuthResponse result = authService.createUser(userRequest);
        assertThat(result.getToken()).isNull();
        assertThat(result.getEmail()).isEqualTo("AZ@in.ir");
        assertThat(result.getRole()).isEqualTo(Roles.USER);

    }






}
