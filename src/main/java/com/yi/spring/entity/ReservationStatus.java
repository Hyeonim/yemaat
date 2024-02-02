package com.yi.spring.entity;

// 예약 상태에 따라 분기가 필요
public enum ReservationStatus {
    NONE, // 아무것도 입력되지 않았을 경우 들어가는 기본값. 현장 손님으로 가정
    WAIT, // 예약을 신청한 직후 (예약 승인을 기다리는 상태)
    USER_CANCEL, // 사용자가 예약을 취소한 상태
    COMPLETED, // 예약 완료 상태
    // 이후의 상태는 시간으로 판단
}