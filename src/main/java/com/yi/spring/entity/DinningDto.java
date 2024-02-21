package com.yi.spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link Dinning}
 */
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DinningDto implements Serializable {
    private int restNo;
    private String restName;
    private String restAddr;
    private String restTel;
    private String restSeat;
    private String restTime;
    private String restOffDays;
    private String restParking;
    private String restMenu;
    private String restCategory;
    private Double restLatitude;
    private Double restLongitude;
    private String restDescription;
//    private User userNo;
    private String restStartDate;
    private String restStatus;

    public DinningDto( Object[] args )
    {
        this( (int)args[0],
                (String)args[1],
                (String)args[2],
                (String)args[3],
                (String)args[4],
                (String)args[5],
                (String)args[6],
                (String)args[7],
                (String)args[8],
                (String)args[9],
                (Double)args[10],
                (Double)args[11],
                (String)args[12],
//                (User)args[13],
                (String)args[14],
                (String)args[15]
                );
    }

    public String getBase64Image()
    {
        return "";
    }
    public Double getRestScore()
    {
        return null;
    }
}