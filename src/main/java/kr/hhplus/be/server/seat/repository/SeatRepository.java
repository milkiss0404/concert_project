package kr.hhplus.be.server.seat.repository;

import kr.hhplus.be.server.concert.application.dtos.ConcertSeatInfoRequest;
import kr.hhplus.be.server.seat.domain.ReservationStatus;
import kr.hhplus.be.server.seat.domain.Seat;
import kr.hhplus.be.server.seat.domain.Zone;
import kr.hhplus.be.server.seat.repository.entity.SeatEntity;

import java.util.List;

public interface SeatRepository {
    SeatEntity seatInfo(Long concertId,Long seatId);

    SeatEntity save(SeatEntity seatEntity);

    List<SeatEntity> findSeatsByZoneAndConcertIdAndStatus(Long concertId, Zone zone, ReservationStatus status);
}
