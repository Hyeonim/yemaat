package com.yi.spring.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
public class ReservationController {

    @GetMapping("/reserve/{restNo}")
    public String reserve(Model model, HttpSession httpSession, @PathVariable String restNo){

        String strRestStart = "10:00";
        String strRestEnd= "22:00";

        LocalTime rest_start = LocalTime.parse(strRestStart, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime rest_end = LocalTime.parse(strRestEnd, DateTimeFormatter.ofPattern("HH:mm"));

        LocalDateTime now = LocalDateTime.now();
        LocalTime currentTime = now.toLocalTime();

        LocalTime laterTime = currentTime.isAfter(rest_start) ? currentTime : rest_start;

        int minutesToAdd = (30 - laterTime.getMinute() % 30) % 30;  // 30의 배수로 맞추기 위해 분을 조정

        LocalTime adjustedTime = laterTime.plusMinutes(minutesToAdd);

        model.addAttribute("rest_time_start", adjustedTime.format(DateTimeFormatter.ofPattern("HH:mm")));
//        model.addAttribute("rest_time_start", adjustedTime );

        Duration duration = Duration.between(adjustedTime, rest_end);
        long minutesDiff = duration.toMinutes();
        long halfCount = minutesDiff / 30;
        model.addAttribute( "rest_time_end", rest_end );
        model.addAttribute( "rest_time_count", halfCount );



        model.addAttribute( "rest_name", "돝고기506" );

        return "reservation/reservation";
    }
}
