package kr.hhplus.be.server.seat.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.repository.entity.ConcertEntity;
import kr.hhplus.be.server.seat.repository.entity.SeatEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Seat {
    private Long id;
    private Concert concert;
    private Zone zone;
    private String seatRow;
    private int seatNumber;
    private ReservationStatus reservationStatus = ReservationStatus.AVAILABLE;

    public int getSeatPrice() {
        return zone.getPrice();
    }
    public void changeSeatZone(Zone zone) {
        this.zone = zone;
    }

    public void holdingStatus() {
        if (this.reservationStatus != ReservationStatus.AVAILABLE) {
            throw new IllegalStateException("이미 예약된 좌석입니다.");
        }
        this.reservationStatus = ReservationStatus.HOLDING;
    }
    public void reserveStatus() {
        this.reservationStatus = ReservationStatus.RESERVED;
    }

    public void cancelStatus() {
        this.reservationStatus = ReservationStatus.AVAILABLE;
    }
}
