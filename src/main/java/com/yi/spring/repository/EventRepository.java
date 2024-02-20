package com.yi.spring.repository;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.Event;
import com.yi.spring.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByRestNo(Dinning restNo);

    @Query(value="select * from event e where DATE(e.write_date) >= CURDATE()-7", nativeQuery = true)
    List<Event> getNewEvents();

    @Query("SELECT e from Event e order by e.writeDate desc limit 5")
    List<Event> getList();

}
