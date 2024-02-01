package com.yi.spring.service;

import com.yi.spring.entity.Menu;
import com.yi.spring.entity.User;
import com.yi.spring.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUserNo(Integer userNo) {
        return userRepository.findByUserNo(userNo);
    }

    @Override
    public long deleteByUserNo(Integer userNo) {
        return userRepository.deleteByUserNo(userNo);
    }

    @Override
    public User updateMenu(User user) {
        User existingMenu = userRepository.findById(user.getUserNo()).orElse(null);

        assert existingMenu != null;



        return userRepository.save(existingMenu);
    }


}
