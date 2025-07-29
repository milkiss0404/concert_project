package kr.hhplus.be.server.seat.repository;

import kr.hhplus.be.server.seat.domain.Seat;

public interface SeatRepository {
    Seat seatInfo(Long concertId , Long seatId);

    void save(Seat seat);
}
