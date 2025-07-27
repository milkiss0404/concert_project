package kr.hhplus.be.server.seat.repository;

import kr.hhplus.be.server.seat.domain.Seat;
import kr.hhplus.be.server.seat.modelMapper.SeatModelMapper;
import kr.hhplus.be.server.seat.repository.entity.SeatEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {
    private final JpaSeatRepository jpaSeatRepository;
    private final SeatModelMapper modelMapper;
    @Override
    public Seat seatInfo(Long concertId, Long seatId) {

        SeatEntity seatEntity = jpaSeatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("해당 좌석이 존재하지 않습니다."));

        if (!seatEntity.getConcert().getId().equals(concertId)) {
            throw new IllegalArgumentException("좌석이 해당 콘서트에 속하지 않습니다.");
        }
        return modelMapper.toDomain(seatEntity);
    }

    @Override
    public void save(Seat seat) {
        seat.reserveStatus();
        jpaSeatRepository.save(modelMapper.toEntity(seat));
    }

}
