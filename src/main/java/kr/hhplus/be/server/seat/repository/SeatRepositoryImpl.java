package kr.hhplus.be.server.seat.repository;

import kr.hhplus.be.server.concert.application.dtos.ConcertSeatInfoRequest;
import kr.hhplus.be.server.seat.domain.ReservationStatus;
import kr.hhplus.be.server.seat.domain.Seat;
import kr.hhplus.be.server.seat.domain.Zone;
import kr.hhplus.be.server.seat.modelMapper.SeatModelMapper;
import kr.hhplus.be.server.seat.repository.entity.SeatEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {
    private final JpaSeatRepository jpaSeatRepository;

    @Override
    public SeatEntity seatInfo(Long concertId,Long seatId) {

        SeatEntity seatEntity = jpaSeatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("해당 좌석이 존재하지 않습니다."));

        if(seatEntity.getReservationStatus() == ReservationStatus.RESERVED){
            throw new RuntimeException("이미 예약됨");
        }
        if (!seatEntity.getConcert().getId().equals(concertId)) {
            throw new IllegalArgumentException("좌석이 해당 콘서트에 속하지 않습니다.");
        }
        return seatEntity;
    }

    @Override
    public SeatEntity save(SeatEntity seatEntity) {
        return jpaSeatRepository.save(seatEntity);
    }

    @Override
    public List<SeatEntity> findSeatsByZoneAndConcertIdAndStatus(Long concertId, Zone zone, ReservationStatus status) {
       return jpaSeatRepository.findSeatsByZoneAndConcertIdAndStatus(concertId,zone, status);
    }
}
