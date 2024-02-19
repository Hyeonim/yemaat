package com.yi.spring.service;

import com.yi.spring.entity.Reservation;
import com.yi.spring.entity.ReservationStatus;
import com.yi.spring.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void processReservations(List<Reservation> list) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        Collections.sort(list, Comparator.comparing(Reservation::getResTime).reversed());

        for (Reservation reservation : list) {
            Timestamp resTimestamp = Timestamp.valueOf(reservation.getResTime());

            if (ReservationStatus.RESERVE_COMPLETED.name().equals(reservation.getRes_status())) {
                if (currentDateTime.isAfter(resTimestamp.toLocalDateTime())) {
                    updateReservationStatus(reservation, ReservationStatus.EXPIRED.name());
                }
            } else if (ReservationStatus.WAIT.name().equals(reservation.getRes_status())) {
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

    public void checkReservationStatus(List<Reservation> list, Model model) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        Reservation latestReservation = list.stream()
                .filter(reservation -> ReservationStatus.RESERVE_COMPLETED.name().equals(reservation.getRes_status())
                        || ReservationStatus.WAIT.name().equals(reservation.getRes_status()))
                .max(Comparator.comparing(Reservation::getResTime))
                .orElse(null);

        if (latestReservation != null) {
            Timestamp resTimestamp = Timestamp.valueOf(latestReservation.getResTime());

            if (ReservationStatus.RESERVE_COMPLETED.name().equals(latestReservation.getRes_status())) {
                model.addAttribute("statusMessage", "Reservation Completed");
                if (currentDateTime.isAfter(resTimestamp.toLocalDateTime())) {
                }
            } else if (ReservationStatus.WAIT.name().equals(latestReservation.getRes_status())) {
                model.addAttribute("statusMessage", "Waiting for Confirmation");
                if (currentDateTime.isAfter(resTimestamp.toLocalDateTime())) {
                }
            }

            model.addAttribute("latestReservation", latestReservation);
        } else {
            model.addAttribute("statusMessage", "No Reservation");
        }
    }

    public void filterReservationStatus(List<Reservation> list, Model model) {
        List<String> filteredStatusMessages = new ArrayList<>();

        for (Reservation reservation : list) {
            if (CollectionUtils.containsAny(
                    Collections.singletonList(reservation.getRes_status()),
                    Arrays.asList(ReservationStatus.RESERVE_COMPLETED.name(),
                            ReservationStatus.WAIT.name()))) {
                filteredStatusMessages.add(reservation.getRes_status());
            }
        }

        model.addAttribute("filteredStatusMessages", filteredStatusMessages);
    }
}
