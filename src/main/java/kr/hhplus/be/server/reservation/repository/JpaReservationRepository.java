package kr.hhplus.be.server.reservation.repository;

import kr.hhplus.be.server.reservation.repository.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReservationRepository extends JpaRepository<ReservationEntity,Long> {
}
