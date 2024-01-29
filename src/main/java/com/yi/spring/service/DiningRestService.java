package com.yi.spring.service;

import com.yi.spring.entity.DiningRest;

import java.util.List;

public interface DiningRestService {
    List<DiningRest> getAllRestaurants();
    DiningRest getRestByRest_no(int rest_no);
    DiningRest createRestaurant(DiningRest diningRest);
    DiningRest updateRestaurant(DiningRest diningRest);
    void deleteRestaurant(int rest_no);
}
