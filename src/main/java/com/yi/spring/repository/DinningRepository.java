package com.yi.spring.repository;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.TablingDto;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DinningRepository extends JpaRepository<Dinning, Long>, JpaSpecificationExecutor<Dinning> {


    List<Dinning> findAll();

    @Query("SELECT d FROM Dinning d WHERE d.restName LIKE %:keyword% and d.restMenu")
    List<Dinning> findByRestNameContaining(@Param("keyword") String keyword);

    //식당 id로 찾는거


}
