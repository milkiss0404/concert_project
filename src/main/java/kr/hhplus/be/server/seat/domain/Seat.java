package kr.hhplus.be.server.seat.domain;

import kr.hhplus.be.server.seat.repository.entity.SeatEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Seat {
    private Long id;
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
        this.reservationStatus = ReservationStatus.HOLDING;
    }
    public void reserveStatus() {
        this.reservationStatus = ReservationStatus.RESERVED;
    }

    public static SeatEntity toSeatEntity(Seat seat) {
        return SeatEntity.builder()
                .id(seat.id)
                .zone(seat.zone)
                .seatRow(seat.seatRow)
                .seatNumber(seat.seatNumber)
                .reservationStatus(seat.reservationStatus)
                .build();

    }
    public void cancelStatus() {
        this.reservationStatus = ReservationStatus.CANCELLED;
    }
}
