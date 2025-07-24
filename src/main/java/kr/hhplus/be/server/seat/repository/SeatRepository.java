package kr.hhplus.be.server.seat.repository;

import kr.hhplus.be.server.seat.domain.Seat;

public interface SeatRepository {
    Seat seatInfo(Long ConcertId , Long seatId);
}
