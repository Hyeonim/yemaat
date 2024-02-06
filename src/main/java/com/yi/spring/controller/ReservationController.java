package com.yi.spring.controller;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.Reservation;
import com.yi.spring.entity.ReservationStatus;
import com.yi.spring.repository.DinningRepository;
import com.yi.spring.repository.ReservationRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/reserve/*")
public class ReservationController {

    @Autowired
    DinningRepository dinningRepository;
    @Autowired
    ReservationRepository reservationRepository;


    @GetMapping("/setUserNo/{userNo}")
    public void setUserNo( HttpSession httpSession, @PathVariable String userNo ) {
        httpSession.setAttribute("userNo", userNo);
    }
    @GetMapping("/{restNo}")
    public String reserve(Model model, HttpSession httpSession, @PathVariable String restNo){
        httpSession.setAttribute("restNo", restNo);

        Long iRestNo = Long.valueOf(restNo);

        Dinning restaurant = dinningRepository.findById( iRestNo ).get();


        String strRestTime = restaurant.getRestTime();

        // 정규표현식 패턴
        // 정규표현식에 대한 매처 생성
        Matcher matcher = Pattern.compile(
                "([0-9]{2}).*([0-9]{2}).*([0-9]{2}).*([0-9]{2})"
            ).matcher( strRestTime );




        String strRestStart = "";
        String strRestEnd= "";

        // 매칭된 부분 추출
        if (matcher.find() && 4 <= matcher.groupCount() ) {
            strRestStart = matcher.group(1) + ":" + matcher.group(2);
            strRestEnd = matcher.group(3) + ":" + matcher.group(4);
        } else if ( "24시간".equals( strRestTime )) {
            strRestStart = "00:00";
            strRestEnd = "23:59";
        }






        LocalTime rest_start = LocalTime.parse(strRestStart, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime rest_end = LocalTime.parse(strRestEnd, DateTimeFormatter.ofPattern("HH:mm"));

        LocalDateTime now = LocalDateTime.now();
        LocalTime currentTime = now.toLocalTime();

        LocalTime laterTime = currentTime.isAfter(rest_start) ? currentTime : rest_start;

        int minutesToAdd = (30 - laterTime.getMinute() % 30) % 30;  // 30의 배수로 맞추기 위해 분을 조정

        LocalTime adjustedTime_start = laterTime.plusMinutes(minutesToAdd).truncatedTo(ChronoUnit.MINUTES);

        model.addAttribute("rest_time_start", adjustedTime_start.format(DateTimeFormatter.ofPattern("HH:mm")));
//        model.addAttribute("rest_time_start", adjustedTime );

        Duration duration = Duration.between(adjustedTime_start, rest_end);
        long minutesDiff = duration.toMinutes();
        long halfCount = minutesDiff / 30;
        model.addAttribute( "rest_time_end", rest_end );
        model.addAttribute( "rest_time_count", halfCount );

        model.addAttribute( "rest_no", restNo );
        model.addAttribute( "rest_name", restaurant.getRestName() );




        String strRestSeat = restaurant.getRestSeat();

        int seatNormal = 0;
        int seatRoom = 0;

        if ( null != strRestSeat ){

            // 정규표현식 패턴
            // 정규표현식에 대한 매처 생성
            Matcher matcherSeat = Pattern.compile(
                    "([0-9]+).*[^(]\\(([0-9]+)실"
            ).matcher( strRestSeat );

            // if (matcherSeat.matches()) {
            if (matcherSeat.find()) {
                seatNormal = Integer.parseInt(matcherSeat.group(1));
                if ( 2 <= matcherSeat.groupCount() )
                    seatRoom = Integer.parseInt(matcherSeat.group(2));
            } else {
                Matcher matcherSeat2 = Pattern.compile(
                        "([0-9]+)"
                ).matcher(strRestSeat);

                if (matcherSeat2.find()) {
                    seatNormal = Integer.parseInt(matcherSeat2.group(1));
                } else {
                    System.out.println("매칭되는 부분이 없습니다.");
                }
            }
        }

//        System.out.println( strRestSeat + "," + seatNormal + "," + seatRoom );

        model.addAttribute( "rest_seat_normal", seatNormal );
        model.addAttribute( "rest_seat_room", seatRoom );


        List<Reservation> reservationList = reservationRepository.findByRestNo( iRestNo );

        int reservePeopleCount = 0;
        int reserveRoomCount = 0;
        for ( Reservation elem : reservationList )
        {
//            if ( null == elem.getRes_status() || Integer.parseInt( elem.getRes_status() ) < 2 )
//                continue;
//            if ( elem.getReservationStatusEnum() != ReservationStatus.NONE && elem.getReservationStatusEnum() != ReservationStatus.COMPLETED )
            if ( !Arrays.asList(ReservationStatus.NONE, ReservationStatus.COMPLETED).contains(elem.getReservationStatusEnum()))
                continue;

            reservePeopleCount += Integer.parseInt( elem.getRes_guest_count() );

            if ( null != elem.getRes_table_type() && "true".equals( elem.getRes_table_type() ) )
                reserveRoomCount += 1;
        }

        model.addAttribute( "reservePeopleCount", reservePeopleCount );
        model.addAttribute( "reserveRoomCount", reserveRoomCount );



//        model.addAttribute( "reservationList", reservationList );
//        adjustedTime_start
//        rest_end

//        LocalDateTime now = LocalDateTime.now();
//        List<List<Reservation>> reservationListByTime = new ArrayList<>();
        class _TempDto{ public LocalTime date; public int count; public String toString(){return date + "," + count;}}
        List<_TempDto> reservationListByTime = new ArrayList<>();

        for ( LocalTime loopTime = adjustedTime_start; loopTime.isBefore( rest_end )
                ; loopTime = loopTime.plusMinutes(30)
        )
        {
//            List<Reservation> partTimeList = new ArrayList<>();
            _TempDto tempDto = new _TempDto();
            tempDto.date = null;
            tempDto.count = 0;

            for ( Reservation elem : reservationList )
            {
                LocalDateTime res_time_withDate = null != elem.getRes_time() ? elem.getRes_time() : now;
                if ( now.isBefore( res_time_withDate ) )
                    continue;
                if ( !Arrays.asList(ReservationStatus.NONE, ReservationStatus.COMPLETED).contains(elem.getReservationStatusEnum()))
                    continue;

                LocalTime res_time = res_time_withDate.toLocalTime();
                if ( false == loopTime.isBefore( res_time ) &&
                         loopTime.plusMinutes( 30 ).isBefore( res_time.plusMinutes( 60* 2 ) )
                )
                {
//                    partTimeList.add( new Reservation() );
//                    Reservation listElem = partTimeList.get( partTimeList.size() - 1 );

                    if ( null == tempDto.date )
                        tempDto.date = loopTime;
                    tempDto.count += Integer.parseInt( elem.getRes_guest_count() );
//                    listElem.setRes_guest_count( elem.getRes_guest_count() );
//                    listElem.setRes_time( null == elem.getRes_time() ? res_time_withDate : elem.getRes_time() );
//                    listElem.setRes_time_new( null == elem.getRes_time_new() ? LocalDateTime.from(loopTime.atDate(LocalDate.now())) : elem.getRes_time_new() );
                }

            }

            if ( 1 <= tempDto.count )
//            reservationListByTime.add( partTimeList );
            reservationListByTime.add( tempDto );



            System.out.println( loopTime );
            System.out.println( reservationListByTime );
        }
        model.addAttribute( "reservationList", reservationListByTime );














        return "reservation/reservation";
    }




    @PostMapping("/insert/")
    public ResponseEntity<String> handleJsonData(@RequestBody Map<String, Object> jsonData, HttpSession session ) {
        Long iRestNo = Optional.ofNullable((String) session.getAttribute("restNo"))
                .map(Long::valueOf).orElse(null);
        Long iUserNo = Optional.ofNullable((String)session.getAttribute("userNo"))
                .map(Long::valueOf).orElse(null);

        String fieldCount = (String) jsonData.get("count");
        String time = (String) jsonData.get("time");
        String date = (String) jsonData.get("date");
        String message = (String) jsonData.get("msg");
        String checked = (String) jsonData.get("b");

        String count = "";
        {
            Matcher matcher = Pattern.compile("([0-9]+)").matcher( fieldCount );
            if (matcher.find())
                count = matcher.group(1);
        }

        Long reservationNo = null;
        {
            List<Reservation> list = reservationRepository.findByRestNoAndUserNo( iRestNo, iUserNo );
            if ( null != list && !list.isEmpty())
                reservationNo = list.get(0).getRes_no();
        }

        Reservation reservation = new Reservation();
        reservation.setRes_no( reservationNo );
        reservation.setRestNo( iRestNo );
        reservation.setUserNo( iUserNo );
        reservation.setRes_time( LocalDateTime.parse(date + " " + time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        reservation.setRes_time_new( LocalDateTime.now() );
        reservation.setRes_guest_count( count );
        reservation.setRes_comment( message );
        reservation.setRes_status(String.valueOf(ReservationStatus.WAIT));
        reservation.setRes_table_type( String.valueOf(checked));
        reservationRepository.save( reservation );

        return new ResponseEntity<>("", HttpStatus.OK);
    }




}