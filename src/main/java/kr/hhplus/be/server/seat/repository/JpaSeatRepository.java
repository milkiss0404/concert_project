package kr.hhplus.be.server.seat.repository;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.seat.domain.ReservationStatus;
import kr.hhplus.be.server.seat.domain.Zone;
import kr.hhplus.be.server.seat.repository.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaSeatRepository extends JpaRepository<SeatEntity,Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<SeatEntity> findById(Long seatId);


    @Query("SELECT s FROM SeatEntity s WHERE s.zone = :zone AND s.concert.id = :concertId AND s.reservationStatus = :status")
    List<SeatEntity> findSeatsByZoneAndConcertIdAndStatus(@Param("concertId") Long concertId,
                                                          @Param("zone") Zone zone,
                                                          @Param("status") ReservationStatus status);
}
