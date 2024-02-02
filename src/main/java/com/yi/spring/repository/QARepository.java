package com.yi.spring.repository;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.QA;
import com.yi.spring.entity.Review;
import com.yi.spring.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QARepository extends PagingAndSortingRepository<QA, Integer> {

    List<QA> findByUserNo(User user);



}
