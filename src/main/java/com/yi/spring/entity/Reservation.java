package com.yi.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "res_no", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Long res_no;

    private LocalDateTime res_time_new;
    private LocalDateTime res_time;

    @Column(name = "user_no")
    private Long userNo;

    @Column(name = "rest_no")
    private Long restNo;

    private String res_guest_count;
    private String res_status;
    private String res_comment;
    private String res_table_type;


    public ReservationStatus getReservationStatusEnum() {
        try {
            return ReservationStatus.valueOf(res_status);
        } catch (Exception e) {
            // 예외 처리: 디비에 저장된 값이 Enum에 존재하지 않을 경우
            return ReservationStatus.NONE; // 또는 다른 기본값 설정
        }
    }
}
