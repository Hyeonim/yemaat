package com.yi.spring.repository;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiningRestRepository extends JpaRepository<Dinning, Integer> {

    Optional<Dinning> findByUserNo(User userNo);

    Optional<Dinning> findByUserNoAndRestStatusNot(User userNo, String restStatus);


    Page<Dinning> findByRestNameContainingIgnoreCase(String name, Pageable pageable);

    // 방금 추가
    @Query("SELECT d FROM Dinning d JOIN FETCH d.userNo u WHERE u.userAuth = '2'")
    List<Dinning> findAllByUserAuthIsOwner();
}
