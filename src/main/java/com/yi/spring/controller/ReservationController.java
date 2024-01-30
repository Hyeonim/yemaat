package com.yi.spring.controller;

import com.yi.spring.entity.Dinning;
import com.yi.spring.repository.DinningRepository;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class ReservationController {

    @Autowired
    DinningRepository dinningRepository;
    @GetMapping("/reserve/{restNo}")
    public String reserve(Model model, HttpSession httpSession, @PathVariable String restNo){

        Dinning restaurant = dinningRepository.findById( Long.valueOf(restNo) ).get();


        String strRestTime = restaurant.getRestTime();

        // 정규표현식 패턴
        String regex = "([0-9]{2}).*([0-9]{2}).*([0-9]{2}).*([0-9]{2})";
        Pattern pattern = Pattern.compile(regex);

        // 정규표현식에 대한 매처 생성
        Matcher matcher = pattern.matcher(strRestTime);



        String strRestStart = "";
        String strRestEnd= "";

        // 매칭된 부분 추출
        if (matcher.matches() && 4 <= matcher.groupCount() ) {
            strRestStart = matcher.group(1) + ":" + matcher.group(2);
            strRestEnd = matcher.group(3) + ":" + matcher.group(4);
        } else if ( "24시간".equals( strRestTime )) {
            strRestStart = "00:00";
            strRestEnd = "23:59";
        };






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



        model.addAttribute( "rest_name", restaurant.getRestName()  );

        return "reservation/reservation";
    }
}
