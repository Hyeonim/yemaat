package com.yi.spring.repository;

import com.yi.spring.entity.Dinning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiningRestRepository extends JpaRepository<Dinning, Integer> {
}
