package com.yi.spring.repository;

import com.yi.spring.entity.Dinning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapRepository extends JpaRepository<Dinning, Long> {


    List<Dinning> findAll();


}
