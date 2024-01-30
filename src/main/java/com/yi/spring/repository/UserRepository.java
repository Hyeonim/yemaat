package com.yi.spring.repository;

import com.yi.spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    List<User> findAll();

    Optional<User> findByUserNo(Integer userNo);

    long deleteByUserNo(Integer userNo);



//    Optional<User> findByUsername(String username);
//
//    Optional<User> findByEmail(String email);

}