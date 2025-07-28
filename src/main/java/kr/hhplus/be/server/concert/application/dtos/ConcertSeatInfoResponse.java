package kr.hhplus.be.server.concert.application.dtos;

import kr.hhplus.be.server.seat.domain.ReservationStatus;
import kr.hhplus.be.server.seat.domain.Seat;
import kr.hhplus.be.server.seat.domain.Zone;

public record ConcertSeatInfoResponse(
        Long id,
        Zone zone,
        String row,
        int seatNumber,
        ReservationStatus reservationStatus
) {
    public static ConcertSeatInfoResponse from(Seat seat) {
        return new ConcertSeatInfoResponse(
                seat.getId(),
                seat.getZone(),
                seat.getSeatRow(),
                seat.getSeatNumber(),
                seat.getReservationStatus()
        );
    }
}