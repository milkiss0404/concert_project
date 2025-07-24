package kr.hhplus.be.server.reservation.domain;

import kr.hhplus.be.server.User.domain.User;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.seat.domain.ReservationStatus;
import kr.hhplus.be.server.seat.domain.Seat;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("예약 테스트")
class ReservationTest {

    Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = Reservation.create(new User(), new Seat(), new Concert());
    }

    @Test
    @DisplayName("예약 하기")
    void reserve() {
        // given
        // when
        reservation.reserve();
        // then
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.RESERVED);
        assertThat(reservation.getReservedAt())
                .isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
        //1초의 오차는 허용한다

    }

    @Test
    @DisplayName("예약 취소하기")
    void cancel() {
        // given
        // when
        reservation.cancel();
        // then
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CANCELLED);
        assertThat(reservation.getCanceledAt())
                .isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
        //1초의 오차는 허용한다
    }

    @Test
    @DisplayName("예약 되었는지 여부 확인")
    void confirm() {
        // given
        // when
        reservation.reserve();
        boolean reserved = reservation.isReserved();
        // then
        assertThat(reserved).isEqualTo(true);
    }


}
