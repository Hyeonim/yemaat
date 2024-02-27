package com.yi.spring.service;

import com.yi.spring.entity.Review;
import com.yi.spring.entity.User;
import org.springframework.data.domain.Page;

public interface ReviewService {

    Page<Review> findAll(int page);
    Page<Review> findByUserNoOrderByRevWriteTimeDesc(User user, int page);
}
