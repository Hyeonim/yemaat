package com.yi.spring.service;

import com.yi.spring.entity.Reservation;
import com.yi.spring.entity.ReservationStatus;
import com.yi.spring.entity.Review;
import com.yi.spring.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void processReservations(List<Reservation> list, Model model) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        Collections.sort(list, Comparator.comparing(Reservation::getResTime).reversed());

        for (Reservation reservation : list) {
            Timestamp resTimestamp = Timestamp.valueOf(reservation.getResTime());

            if (ReservationStatus.RESERVE_COMPLETED.name().equals(reservation.getRes_status())) {
                model.addAttribute("statusMessage", "OK"); // "OK" 데이터 추가
                if (currentDateTime.isAfter(resTimestamp.toLocalDateTime())) {
                    updateReservationStatus(reservation, ReservationStatus.EXPIRED.name());
                }
            } else if (ReservationStatus.WAIT.name().equals(reservation.getRes_status())) {
                model.addAttribute("statusMessage", "READY"); // "READY" 데이터 추가
                if (currentDateTime.isAfter(resTimestamp.toLocalDateTime())) {
                    updateReservationStatus(reservation, ReservationStatus.REST_CANCEL.name());
                }
            }
        }
    }

    private void updateReservationStatus(Reservation reservation, String newStatus) {
        reservation.setRes_status(newStatus);
        reservationRepository.save(reservation);
    }
}
