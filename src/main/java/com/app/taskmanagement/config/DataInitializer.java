package com.app.taskmanagement.config;

import com.app.taskmanagement.dto.mapper.UserMapper;
import com.app.taskmanagement.model.User;
import com.app.taskmanagement.model.enums.Roles;
import com.app.taskmanagement.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.userMapper=userMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        if(!userRepository.existsUserByEmail("azishsam74@Gmail.com")){
            User user = new User("Peyman", "Azish", "azishsam74@Gmail.com",passwordEncoder.encode("123"), Roles.ADMIN);
            userRepository.save(user);
        }

    }
}
