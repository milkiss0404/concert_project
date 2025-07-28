package kr.hhplus.be.server.concert.repository;

import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.repository.entity.ConcertEntity;

public interface ConcertRepository {
    Concert findById(Long id);
}
