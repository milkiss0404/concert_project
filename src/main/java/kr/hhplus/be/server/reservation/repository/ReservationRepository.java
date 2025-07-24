package kr.hhplus.be.server.reservation.repository;

import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.seat.domain.Seat;

public interface ReservationRepository {
    void reserve(User user, Concert concert, Seat seat);
}
