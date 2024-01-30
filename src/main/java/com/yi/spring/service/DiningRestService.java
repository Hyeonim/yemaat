package com.yi.spring.service;

import com.yi.spring.entity.DiningRest;
import com.yi.spring.entity.Dinning;

import java.util.List;

public interface DiningRestService {
    List<Dinning> getAllRestaurants();
    Dinning getRestByRest_no(int rest_no);
    Dinning createRestaurant(Dinning dinning);
    Dinning updateRestaurant(Dinning dinning);
    void deleteRestaurant(int rest_no);
}
