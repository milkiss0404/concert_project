package kr.hhplus.be.server.seat.repository;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.seat.repository.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaSeatRepository extends JpaRepository<SeatEntity,Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<SeatEntity> findById(Long seatId);

}
