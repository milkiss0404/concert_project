package kr.hhplus.be.server.seat.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("좌석 테스트")
@Nested
class SeatTest {
    Seat seat;

    @BeforeEach
    void setup() {
        seat = Seat.builder()
                .id(1L)
                .zone(Zone.VIP)
                .row("2열")
                .seatNumber(20)
                .reservationStatus(ReservationStatus.RESERVED).build();
    }

    @Test
    @DisplayName("등급별 좌석 가격 구하기")
    void selectSeatPrice () {
        // given
        // when
        int seatPrice = seat.getSeatPrice();
        // then
        assertEquals(SeatGrade.VIP.getPrice(),seatPrice);
    }

    @Test
    @DisplayName("좌석 등급 바꾸기")
    void changeSeatZone () {
        // given
        // when
        seat.changeSeatZone(Zone.S);
        // then
        assertEquals(Zone.S, seat.getZone());
    }
    @Test
    @DisplayName("좌석 예약 상태 바꾸기")
    void changeSeatGrade () {
        // given
        // when
        seat.changeStatus(ReservationStatus.AVAILABLE);
        // then
        assertEquals(ReservationStatus.AVAILABLE,seat.getReservationStatus());
    }

}