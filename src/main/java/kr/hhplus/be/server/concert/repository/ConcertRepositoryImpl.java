package kr.hhplus.be.server.concert.repository;

import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.repository.entity.ConcertEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {
    private final JpaConcertRepository jpaConcertRepository;

    @Override
    public Concert findById(Long id) {
        ConcertEntity concertEntity = jpaConcertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 콘서트입니다"));
        return concertEntity.toConcert();
    }
}

