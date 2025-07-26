package kr.hhplus.be.server.reservation.repository;

import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.reservation.domain.Reservation;
import kr.hhplus.be.server.reservation.repository.entity.ReservationEntity;
import kr.hhplus.be.server.seat.domain.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ReservationRepositoryImpl implements ReservationRepository {
    private final JpaReservationRepository jpaReservationRepository;

    @Override
    public void reserve(Reservation reservation) {
        ReservationEntity entity = reservation.toEntity(reservation);
        jpaReservationRepository.save(entity);
    }
}
