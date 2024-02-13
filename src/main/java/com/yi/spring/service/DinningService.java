package com.yi.spring.service;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.User;
import com.yi.spring.repository.DinningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DinningService {

    private static final int PAGE_SIZE = 10; // 페이지당 아이템 수
    private final DinningRepository dinningRepository;

    @Autowired
    public DinningService(DinningRepository dinningRepository) {
        this.dinningRepository = dinningRepository;
    }


//    public List<Object[]> findLatitudeAndLongitude() {
//        return mapRepository.findLatitudeAndLongitude();
//    }

    public List<Dinning> findAll() {
        return dinningRepository.findAll();
    }

    public Page<Dinning> findByDinningNoPaged(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return dinningRepository.findAll(pageable);
    }


    public Page<Dinning> searchByDinningNamePaged(int page, String name) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("restNo").ascending());
        return dinningRepository.findByRestNameContainingIgnoreCase(name, pageable);
    }

}
