package kr.hhplus.be.server.seat.repository;

import kr.hhplus.be.server.concert.application.dtos.ConcertSeatInfoRequest;
import kr.hhplus.be.server.seat.domain.Seat;
import kr.hhplus.be.server.seat.repository.entity.SeatEntity;

public interface SeatRepository {
    SeatEntity seatInfo(Long concertId,Long seatId);

    SeatEntity save(SeatEntity seatEntity);
}
