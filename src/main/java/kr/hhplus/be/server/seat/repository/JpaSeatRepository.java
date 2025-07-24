package kr.hhplus.be.server.seat.repository;

import kr.hhplus.be.server.seat.repository.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSeatRepository extends JpaRepository<SeatEntity,Long> {
}
