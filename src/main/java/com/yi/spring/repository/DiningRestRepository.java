package com.yi.spring.repository;

import com.yi.spring.entity.DiningRest;
import com.yi.spring.service.DiningRestService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiningRestRepository extends JpaRepository<DiningRest, Integer> {
}
