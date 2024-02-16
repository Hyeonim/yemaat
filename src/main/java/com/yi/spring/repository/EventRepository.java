package com.yi.spring.repository;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByRestNo(Dinning restNo);

    @Query(value="select * from event e where DATE(e.event_time) <= CURDATE()", nativeQuery = true)
    List<Event> getNewEvents();
}
