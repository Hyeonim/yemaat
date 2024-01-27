package com.yi.spring.service;

import com.yi.spring.entity.Dinning;
import com.yi.spring.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DinningService {


    private final MapRepository mapRepository;

    @Autowired
    public DinningService(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }


//    public List<Object[]> findLatitudeAndLongitude() {
//        return mapRepository.findLatitudeAndLongitude();
//    }

    public List<Dinning> findAll() {
        return mapRepository.findAll();
    }
}
