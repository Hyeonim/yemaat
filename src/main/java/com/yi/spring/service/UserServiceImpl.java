package com.yi.spring.service;

import com.yi.spring.entity.Menu;
import com.yi.spring.entity.QA;
import com.yi.spring.entity.Reservation;
import com.yi.spring.entity.User;
import com.yi.spring.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUserNo(int userNo) {
        return userRepository.findByUserNo(userNo);
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return userRepository.findByUserId(userId);
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

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

//    @Override
//    public Page<User> findAll(int page) {
//        Pageable pageable = PageRequest.of(page, 10);
//        return this.userRepository.findAll(pageable);
//    }


    @Override
    public Page<User> findByUserNoPaged(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return userRepository.findByUserAuth("1", pageable);
    }



}
