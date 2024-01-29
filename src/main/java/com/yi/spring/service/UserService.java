package com.yi.spring.service;

import com.yi.spring.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);

    User getUserById(Long userId);

    List<User> getAllUsers();

    User updateUser(User user);

    void deleteUser(Long userId);

    Optional<User> getUserEmail(String email);

}
