package com.yi.spring.repository;

import com.yi.spring.entity.QaAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QaAnswerRepository extends JpaRepository<QaAnswer, Integer> {





}
