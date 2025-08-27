package kr.hhplus.be.server.concert.application.userCase;

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
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Level;

@Component
@RequiredArgsConstructor
@Log4j2
public class ReserveUseCase {
    private final ReservationService reservationService;
    private final ConcertService concertService;
    private final SeatService seatService;
    private final UserService userService;

    //UseCase 의 본래 목적에 맞는 핵심 로직은 직접 처리, 그 외 관심사는 이벤트로 분리
    @DistributedLock(key = "'concert:' + #concertId + ':seat:' + #seatId")
    public Seat choiceSeatAndReserve(Long concertId, Long seatId, Long userId) {
        Seat seat = null;
        boolean pointDeducted = false;
        try {
            Concert concert = concertService.findConcert((concertId));
            seat = seatService.seatInfo(concertId, seatId);
            User user = userService.findById(userId);

            //예약처리
            seatService.save(seat);

            Reservation reservation = Reservation.create(user, seat, concert);
            reservationService.reserve(reservation);

            pointDeducted = true;

            return seat;
        } catch (Exception e) {
            compensateReservation(pointDeducted, seat, userId);
            throw new RuntimeException("예약 중 오류 발생", e);
        }
    }

    private void compensateReservation(boolean pointDeducted, Seat seat, Long userId) {
        if (pointDeducted) {
            try {
                userService.refundUserPoint(userId, seat.getSeatPrice());
            } catch (Exception ex) {
                log.error("포인트 환불 실패", ex);
            }
        }
        if (seat != null) {
            try {
                seatService.cancelSeatReservation(seat);
            } catch (Exception ex) {
                log.error("좌석 취소 실패", ex);
            }
        }
    }
}
