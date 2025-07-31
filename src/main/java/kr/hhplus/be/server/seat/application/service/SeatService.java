package kr.hhplus.be.server.seat.application.service;

import kr.hhplus.be.server.concert.application.dtos.ConcertSeatInfoRequest;
import kr.hhplus.be.server.seat.domain.Seat;
import kr.hhplus.be.server.seat.modelMapper.SeatModelMapper;
import kr.hhplus.be.server.seat.repository.SeatRepository;
import kr.hhplus.be.server.seat.repository.entity.SeatEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatService {
    private final SeatRepository seatRepository;
    private final SeatModelMapper seatModelMapper;

    public Seat save(Seat seat) {
        seat.reserveStatus();
        SeatEntity save = seatRepository.save(seatModelMapper.toEntity(seat));
        return seatModelMapper.toDomain(save);
    }
    public Seat seatInfo(Long concertId , Long seatId) {
        SeatEntity seatEntity = seatRepository.seatInfo(concertId,seatId);
        return seatModelMapper.toDomain(seatEntity);
    }
}
