package kr.hhplus.be.server.seat.repository;

import kr.hhplus.be.server.seat.domain.Seat;
import kr.hhplus.be.server.seat.repository.entity.SeatEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {
    private final JpaSeatRepository jpaSeatRepository;
    @Override
    public Seat seatInfo(Long concertId, Long seatId) {
        SeatEntity seatEntity = jpaSeatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("해당 좌석이 존재하지 않습니다."));

        // 콘서트 ID가 일치하는지 검증 (선택적 검증)
        if (!seatEntity.getConcert().getId().equals(concertId)) {
            throw new IllegalArgumentException("좌석이 해당 콘서트에 속하지 않습니다.");
        }
        return seatEntity.toSeat();
    }

}
