package kr.hhplus.be.server.seat.domain;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    HOLDING, // 예약 대기 상태
    RESERVED, // 예약됨
    AVAILABLE, // 예약가능
    CANCELLED //취소됨
}