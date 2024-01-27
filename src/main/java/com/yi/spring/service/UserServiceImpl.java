package com.yi.spring.service;


import com.yi.spring.entity.User;
import com.yi.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User createUser(User user) {


        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId) {
    return null;
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        User existingUser = userRepository.findById(Math.toIntExact(user.getId())).get();
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.deleteById(Math.toIntExact(user.getId()));

    }
//
//    @Override
//    public User getUserEmail(String email) {
//
//        return userRepository.findByEmail(email);
//
//    }

    @Override
    public Optional<User> getUserEmail(String email) {

        return userRepository.findByEmail(email);
    }


}
