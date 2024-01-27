package com.yi.spring.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.Data;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name="dining_rest")
public class Dinning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rest_no")
    private Long restNo;

    @Column(name = "rest_name")
    private String restName;

    @Column(name = "rest_addr")
    private String restAddr;

    @Column(name = "rest_tel")
    private String restTel;

    @Column(name = "rest_seat")
    private String restSeat;

    @Column(name = "rest_time")
    private String restTime;

    @Column(name = "rest_off_days")
    private String restOffDays;

    @Column(name = "rest_parking")
    private String restParking;

    @Lob
    @Column(name = "rest_menu")
    private String restMenu;

    @Column(name = "rest_category")
    private String restCategory;

    @Column(name = "rest_latitude")
    private Double restLatitude;

    @Column(name = "rest_longitude")
    private Double restLongitude;

    @Lob
    @Column(name = "rest_img")
    private byte[] restImg;

    @Column(name = "rest_score")
    private Float restScore;

    @Column(name = "rest_description")
    private String restDescription;

    @Column(name = "user_no")
    private Long userNo;

    @Column(name = "rest_start_date")
    private String restStartDate;

    @Column(name = "rest_status")
    private String restStatus;


}
