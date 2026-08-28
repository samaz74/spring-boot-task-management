package com.app.taskmanagement.repository;

import com.app.taskmanagement.model.User;
import com.app.taskmanagement.model.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {


    List<User> findUserByEmailContaining(String email);

    Optional<User> findUserByEmail(String email);
    List<User> findUserByFNameContains(String fName);
    List<User> findUserByLNameContains(String lName);

    List<User> findUserByRole(Roles role);

    boolean existsUserByEmail(String email);
}
