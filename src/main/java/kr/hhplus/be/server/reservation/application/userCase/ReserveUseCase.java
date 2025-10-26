package kr.hhplus.be.server.reservation.application.userCase;

import kr.hhplus.be.server.concert.application.dtos.ConcertFindRequest;
import kr.hhplus.be.server.concert.application.dtos.ConcertRankingCounterEvent;
import kr.hhplus.be.server.concert.application.dtos.ConcertSeatInfoRequest;
import kr.hhplus.be.server.concert.application.service.ConcertService;
import kr.hhplus.be.server.concert.modelMapper.ConcertModelMapper;
import kr.hhplus.be.server.concert.repository.entity.ConcertEntity;
import kr.hhplus.be.server.queue.redis.DistributedLock;
import kr.hhplus.be.server.reservation.application.service.ReservationService;
import kr.hhplus.be.server.reservation.domain.Reservation;
import kr.hhplus.be.server.seat.application.service.SeatService;
import kr.hhplus.be.server.seat.modelMapper.SeatModelMapper;
import kr.hhplus.be.server.seat.repository.entity.SeatEntity;
import kr.hhplus.be.server.user.application.dto.UsingPointEvent;
import kr.hhplus.be.server.user.application.dto.UsingUserPointRequest;
import kr.hhplus.be.server.user.application.service.UserService;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.modelMapper.UserModelMapper;
import kr.hhplus.be.server.user.repository.UserRepository;
import kr.hhplus.be.server.concert.application.dtos.ChoiceSeatRequest;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.repository.ConcertRepository;
import kr.hhplus.be.server.reservation.repository.ReservationRepository;
import kr.hhplus.be.server.seat.domain.Seat;
import kr.hhplus.be.server.seat.repository.SeatRepository;
import kr.hhplus.be.server.user.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Level;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class ReserveUseCase {
    private final ReservationService reservationService;
    private final ConcertService concertService;
    private final SeatService seatService;
    private final UserService userService;

    public Seat choiceSeatAndReserve(Long concertId, Long seatId, Long userId) {
        // 1. 필요한 정보 조회
        Concert concert = concertService.findConcert(concertId);
        Seat seat = seatService.seatInfo(concertId, seatId);
        User user = userService.findById(userId);

        // 2. 도메인 로직 호출 및 상태 변경
        seat.reserveStatus(); // 좌석 도메인 객체 상태 변경
        Seat updatedSeat = seatService.save(seat); // 좌석 상태 저장

        // 3. 예약 생성 및 처리 (포인트 차감 등)
        Reservation reservation = Reservation.create(user, updatedSeat, concert);
        reservationService.reserve(reservation); // 예약 정보 저장, 포인트 차감 (ReservationService 책임)

        return updatedSeat;
    }
}
