package com.yi.spring.entity.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yi.spring.entity.Dinning;
import com.yi.spring.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * DTO for {@link Dinning}
 */
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DinningDto implements Serializable {
    private final int restNo;
    private final String restName;
    private final String restAddr;
    private final String restTel;
    private final String restSeat;
    private final String restTime;
    private final String restOffDays;
    private final String restParking;
    private final String restMenu;
    private final String restCategory;
    private final Double restLatitude;
    private final Double restLongitude;
    private final String restImg;
    private final String restDescription;
    private final User userNo;
    private final String restStartDate;
    private final String restStatus;
}