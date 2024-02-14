package com.yi.spring.repository;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.Review;
import com.yi.spring.entity.TablingDto;
import com.yi.spring.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DinningRepository extends JpaRepository<Dinning, Long>, JpaSpecificationExecutor<Dinning> {


    List<Dinning> findAll();

    Optional<Dinning> deleteDinningByRestNo(int restNo);

    @Query("SELECT d FROM Dinning d WHERE d.restName LIKE %:keyword%")
    List<Dinning> findByRestNameContaining(@Param("keyword") String keyword);

    Optional<Dinning> findByRestNo(int restNo);

    //식당 id로 찾는거



    @Query("select d from Dinning d where d.restImg is not null order by RAND() LIMIT :sLimit")
    List<Dinning> getRandomList(@Param("sLimit") String sLimit);

    List<Dinning> findByRestNo(Long restNo);

    Page<Dinning> findAll(Pageable pageable);


    Page<Dinning> findByRestNameContainingIgnoreCase(String name, Pageable pageable);


}
