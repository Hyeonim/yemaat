package com.yi.spring.repository;

import com.yi.spring.entity.Dinning;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DinningRepository extends JpaRepository<Dinning, Long>, JpaSpecificationExecutor<Dinning> {


    List<Dinning> findAll();

    //식당 id로 찾는거


}
