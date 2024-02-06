package com.yi.spring.service;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.User;

import java.util.List;

public interface DiningRestService {
    List<Dinning> getAllRestaurants();
    Dinning getRestByRestNo(int restNo);
    void createRestaurant(Dinning dinning);
    Dinning updateRestaurant(Dinning dinning);
    void deleteRestaurant(int restNo);

    Dinning getByUserNo(User userNo);
}
