package kr.hhplus.be.server.seat.domain;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    RESERVED, // 예약됨
    AVAILABLE, // 예약가능
    CANCELLED //취소됨
}