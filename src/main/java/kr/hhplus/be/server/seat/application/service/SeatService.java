package kr.hhplus.be.server.seat.application.service;

import kr.hhplus.be.server.concert.application.dtos.ConcertSeatInfoRequest;
import kr.hhplus.be.server.seat.domain.Seat;
import kr.hhplus.be.server.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;

    @Transactional
    public Seat seatInfo(Long concertId, ConcertSeatInfoRequest seatInfoRequest) {
      return seatRepository.seatInfo(concertId, seatInfoRequest.seatId());
    }
}
