package kr.hhplus.be.server.reservation.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.concert.repository.entity.ConcertEntity;
import kr.hhplus.be.server.seat.repository.entity.SeatEntity;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.reservation.repository.entity.ReservationEntity;
import kr.hhplus.be.server.seat.domain.ReservationStatus;
import kr.hhplus.be.server.seat.domain.Seat;
import kr.hhplus.be.server.user.repository.entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Reservation {
    private Long id;
    private User user;
    private Concert concert;
    private Seat seat;
    private ReservationStatus status;
    private LocalDateTime reservedAt;
    private LocalDateTime canceledAt;


    public static Reservation create(User user, Seat seat, Concert concert) {
        return Reservation.builder()
                .user(user)
                .seat(seat)
                .concert(concert)
                .status(ReservationStatus.RESERVED)
                .reservedAt(LocalDateTime.now())
                .build();
    }

    // 예약 확정
    public void reserve() {
        this.status = ReservationStatus.RESERVED;
        this.reservedAt = LocalDateTime.now();
    }

    // 예약 취소
    public void cancel() {
        if (this.status == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("이미 취소된 예약입니다.");
        }
        this.status = ReservationStatus.CANCELLED;
        this.canceledAt = LocalDateTime.now();
    }

    public int getSeatPrice() {
        return seat.getSeatPrice();
    }

    // 예약 상태 체크
    public boolean isReserved() {
        return this.status == ReservationStatus.RESERVED;
    }

    public Long getConcertId() {
        return concert.getId();
    }
}
