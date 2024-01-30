package com.yi.spring.service;

import com.yi.spring.entity.DiningRest;
import com.yi.spring.entity.Dinning;
import com.yi.spring.repository.DiningRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiningRestServiceImpl implements DiningRestService{
    @Autowired
    private DiningRestRepository diningRestRepository;

    @Override
    public List<Dinning> getAllRestaurants() {
        return diningRestRepository.findAll();
    }

    @Override
    public Dinning getRestByRestNo(int restNo) {
        Optional<Dinning> optionalDiningRest = diningRestRepository.findById(restNo);
        return optionalDiningRest.orElse(null);
    }

    @Override
    public Dinning createRestaurant(Dinning dinning) {
        return diningRestRepository.save(dinning);
    }

    @Override
    public Dinning updateRestaurant(Dinning dinning) {
//        DiningRest existingDiningRest = diningRestRepository.findById(diningRest.getRest_no()).orElse(null);
//        assert existingDiningRest != null;
//        existingDiningRest.setRest_name(diningRest.getRest_name());
        return diningRestRepository.save(dinning);
    }

    @Override
    public void deleteRestaurant(int restNo) {
        diningRestRepository.deleteById(restNo);
    }
}
