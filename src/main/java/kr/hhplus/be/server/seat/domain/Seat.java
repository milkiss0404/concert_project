package kr.hhplus.be.server.seat.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Seat {
    private Long id;
    private Zone zone;
    private String row;
    private int seatNumber;
    private ReservationStatus reservationStatus;

    public int getSeatPrice() {
        return zone.getPrice();
    }
    public void changeSeatZone(Zone zone) {
        this.zone = zone;
    }

    public void changeStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
}
