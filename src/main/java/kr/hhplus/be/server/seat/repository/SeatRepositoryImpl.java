package kr.hhplus.be.server.seat.repository;

import kr.hhplus.be.server.concert.application.dtos.ConcertSeatInfoRequest;
import kr.hhplus.be.server.seat.domain.Seat;
import kr.hhplus.be.server.seat.modelMapper.SeatModelMapper;
import kr.hhplus.be.server.seat.repository.entity.SeatEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {
    private final JpaSeatRepository jpaSeatRepository;

    @Override
    public SeatEntity seatInfo(Long concertId,Long seatId) {

        SeatEntity seatEntity = jpaSeatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("해당 좌석이 존재하지 않습니다."));

        if (!seatEntity.getConcert().getId().equals(concertId)) {
            throw new IllegalArgumentException("좌석이 해당 콘서트에 속하지 않습니다.");
        }
        return seatEntity;
    }

    @Override
    public SeatEntity save(SeatEntity seatEntity) {
        return jpaSeatRepository.save(seatEntity);
    }

}
