package com.yi.spring.repository;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiningRestRepository extends JpaRepository<Dinning, Integer> {

    Optional<Dinning> findByUserNo(User userNo);

    Optional<Dinning> findByUserNoAndRestStatusNot(User userNo, String restStatus);


}
