package kr.hhplus.be.server.reservation.repository;

import kr.hhplus.be.server.reservation.modelmapper.ReservationModelMapper;
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
    public void save(ReservationEntity reservationEntity) {
        jpaReservationRepository.save(reservationEntity);
    }

    @Override
    public ReservationEntity findById(Long id) {
        return jpaReservationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("찾을수없는 예약정보"));
    }
}
