package kr.hhplus.be.server.concert.application.service;

import kr.hhplus.be.server.concert.application.dtos.ConcertFindRequest;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.repository.ConcertRepository;
import kr.hhplus.be.server.concert.repository.entity.ConcertEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    public Concert findConcert(ConcertFindRequest concertFindRequest) {
       return concertRepository.findById(concertFindRequest.concertId());
    }
}

