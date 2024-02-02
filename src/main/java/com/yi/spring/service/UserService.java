package com.yi.spring.service;

import com.yi.spring.entity.Menu;
import com.yi.spring.entity.User;
import org.springframework.data.relational.core.sql.In;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    Optional<User> findByUserNo(Integer userNo);

    long deleteByUserNo(Integer userNo);

    public User updateMenu(User user);




}
