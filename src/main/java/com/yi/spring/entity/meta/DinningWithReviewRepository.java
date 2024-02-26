package com.yi.spring.entity.meta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DinningWithReviewRepository extends JpaRepository<DinningReviewView, Integer>, JpaSpecificationExecutor<DinningReviewView> {

}