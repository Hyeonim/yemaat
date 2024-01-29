package com.yi.spring.service;

import com.yi.spring.entity.Dinning;
import com.yi.spring.repository.DinningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DinningService {


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
}
