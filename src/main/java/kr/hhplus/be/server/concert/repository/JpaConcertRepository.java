package kr.hhplus.be.server.concert.repository;

import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.repository.entity.ConcertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface JpaConcertRepository extends JpaRepository<ConcertEntity,Long> {
}
