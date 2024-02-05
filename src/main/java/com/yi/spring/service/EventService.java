package com.yi.spring.service;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.Event;

import java.util.List;

public interface EventService {

    List<Event> findByRestNo(Dinning restNo);
}
