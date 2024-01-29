package com.yi.spring.service;

import com.yi.spring.entity.DiningRest;
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
    public List<DiningRest> getAllRestaurants() {
        return diningRestRepository.findAll();
    }

    @Override
    public DiningRest getRestByRest_no(int rest_no) {
        Optional<DiningRest> optionalDiningRest = diningRestRepository.findById(rest_no);
        return optionalDiningRest.orElse(null);
    }

    @Override
    public DiningRest createRestaurant(DiningRest diningRest) {
        return diningRestRepository.save(diningRest);
    }

    @Override
    public DiningRest updateRestaurant(DiningRest diningRest) {
//        DiningRest existingDiningRest = diningRestRepository.findById(diningRest.getRest_no()).orElse(null);
//        assert existingDiningRest != null;
//        existingDiningRest.setRest_name(diningRest.getRest_name());
        return diningRestRepository.save(diningRest);
    }

    @Override
    public void deleteRestaurant(int rest_no) {
        diningRestRepository.deleteById(rest_no);
    }
}
