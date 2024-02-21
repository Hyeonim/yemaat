package com.yi.spring.entity;

/**
 * Projection for {@link Dinning}
 */
public interface DinningInfo {
    int getRestNo();

    String getRestName();

    String getRestAddr();

    String getRestTel();

    String getRestSeat();

    String getRestTime();

    String getRestOffDays();

    String getRestParking();

    String getRestMenu();

    String getRestCategory();

    Double getRestLatitude();

    Double getRestLongitude();

    String getRestDescription();

    String getRestStartDate();

    String getRestStatus();

    User getUserNo();

    default String getBase64Image()
    {
        return "";
    }

    Double getRestScore();


}