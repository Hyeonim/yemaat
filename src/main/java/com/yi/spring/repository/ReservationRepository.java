package com.yi.spring.repository;

import com.yi.spring.entity.Reservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByRestNo_RestNo(Long rest_no);

    List<Reservation> findByRestNo_RestNoAndUserNo_UserNo(Long restNo, Long user_no);
    List<Reservation> findByUserNo_UserNo(Long user_no);

    @Query("SELECT r FROM Reservation r WHERE r.userNo = :userNo ORDER BY r.resTime DESC")
    List<Reservation> findLatestReservationByUserNo(@Param("userNo") Long userNo, Pageable pageable);

//    @Query("SELECT dr.restName, dr.restCategory, r.reservationTime, r.guestCount, dr.restAddr " +
//            "FROM Reservation r " +
//            "JOIN r.diningRest dr " +
//            "WHERE r.userNo = :userNo")
//    List<Object[]> findReservationDetailsByUserNo(Long userNo);


    /*
    Query("SELECT dr.restName, dr.restCategory, r.reservationTime, r.guestCount, dr.restAddr " +
            "FROM Reservation r " +
            "JOIN DiningRest dr ON r.restNo = dr.restNo " +
            "WHERE r.userNo = :userNo")
    */
    @Query("SELECT r.restNo.restName, r.restNo.restCategory, r.resTime, r.res_guest_count, r.restNo.restAddr " +
            "FROM Reservation r " +
            "WHERE r.userNo = :userNo")
    List<Reservation> findReservationDetailsByUserNo(Long userNo);



//    List<Reservation> findByRestNo_RestNameAndRestNo_RestCategoryAndResTimeAndRes_guest_countAndRestNo_RestAddr(String restName, String restCategory, LocalDateTime resTime, String res_guest_count, String restAddr);


}
