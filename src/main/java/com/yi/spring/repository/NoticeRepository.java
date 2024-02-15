package com.yi.spring.repository;


import com.yi.spring.entity.Notice;
import com.yi.spring.entity.QA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    List<Notice> findAll();

    Page<Notice> findAll(Pageable pageable);

    Page<Notice> findBySubjectContaining(String keyword, Pageable pageable);


}
