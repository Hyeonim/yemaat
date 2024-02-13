package com.yi.spring.repository;

import com.yi.spring.entity.Reservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByRestNo(Long rest_no);

    List<Reservation> findByRestNoAndUserNo(Long restNo, Long user_no);
    List<Reservation> findByUserNo(Long user_no);

    @Query("SELECT r FROM Reservation r WHERE r.userNo = :userNo ORDER BY r.res_time DESC")
    List<Reservation> findLatestReservationByUserNo(@Param("userNo") Long userNo, Pageable pageable);


}
