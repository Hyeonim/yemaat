package com.yi.spring.controller;

import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.Reservation;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/reserve/*")
public class ReservationController {

    @Autowired
    DinningRepository dinningRepository;
    @Autowired
    ReservationRepository reservationRepository;
    @GetMapping("/{restNo}")
    public String reserve(Model model, HttpSession httpSession, @PathVariable String restNo){
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
            if ( null == elem.getRes_status() || Integer.parseInt( elem.getRes_status() ) < 2 )
                continue;

            System.out.println( elem );
            reservePeopleCount += Integer.parseInt( elem.getRes_guest_count() );

            if ( null != elem.getRes_table_type() && "방".equals( elem.getRes_table_type() ) )
                reserveRoomCount += 1;
        }

        model.addAttribute( "reservePeopleCount", reservePeopleCount );
        model.addAttribute( "reserveRoomCount", reserveRoomCount );



//        model.addAttribute( "reservationList", reservationList );
//        adjustedTime_start
//        rest_end

//        LocalDateTime now = LocalDateTime.now();
        List<List<Reservation>> reservationListByTime = new ArrayList<>();
        for ( LocalTime loopTime = adjustedTime_start; loopTime.isBefore( rest_end )
                ; loopTime = loopTime.plusMinutes(30)
        )
        {
            List<Reservation> partTimeList = new ArrayList<>();

            for ( Reservation elem : reservationList )
            {
                LocalDateTime res_time_withDate = null != elem.getRes_time() ? elem.getRes_time() : now;
                if ( now.isBefore( res_time_withDate ) )
                    continue;

                LocalTime res_time = res_time_withDate.toLocalTime();
                if ( false == loopTime.isBefore( res_time ) &&
                         loopTime.plusMinutes( 30 ).isBefore( res_time.plusMinutes( 60* 2 ) )
                )
                {
                    partTimeList.add( new Reservation() );
                    Reservation listElem = partTimeList.get( partTimeList.size() - 1 );

                    listElem.setRes_guest_count( elem.getRes_guest_count() );
                    listElem.setRes_time( null == elem.getRes_time() ? res_time_withDate : elem.getRes_time() );
                    listElem.setRes_time_new( null == elem.getRes_time_new() ? LocalDateTime.from(loopTime.atDate(LocalDate.now())) : elem.getRes_time_new() );
                }

            }

            reservationListByTime.add( partTimeList );



            System.out.println( loopTime );
            System.out.println( reservationListByTime );
        }
        model.addAttribute( "reservationList", reservationListByTime );














        return "reservation/reservation";
    }




    @PostMapping("/insert/")
    public ResponseEntity<String> handleJsonData(@RequestBody Map<String, Object> jsonData) {
        String key1 = (String) jsonData.get("key1");
        String key2 = (String) jsonData.get("key2");
        String key3 = (String) jsonData.get("key3");
        String key4 = (String) jsonData.get("key4");
        boolean key5 = (boolean) jsonData.get("key5");

        System.out.println( key1 );
        System.out.println( key2 );
        System.out.println( key3 );
        System.out.println( key4 );
        System.out.println( key5 );

        String response = "Success";
        String errorResponse = "Error";

//        return new ResponseEntity<>(response, HttpStatus.OK);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }


}
