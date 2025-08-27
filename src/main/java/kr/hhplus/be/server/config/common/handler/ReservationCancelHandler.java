package kr.hhplus.be.server.config.common.handler;

import kr.hhplus.be.server.reservation.application.dto.CancelReservationEvent;
import kr.hhplus.be.server.reservation.application.service.ReservationService;
import kr.hhplus.be.server.reservation.domain.Reservation;
import kr.hhplus.be.server.seat.application.service.SeatService;
import kr.hhplus.be.server.seat.domain.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ReservationCancelHandler {

    private final ReservationService reservationService;
    private final SeatService seatService;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 포인트 차감 실패 시 예약 취소 이벤트 처리
     */

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCancelReservationEvent(CancelReservationEvent event) {
        Reservation reservation = event.reservation();

        // 1. 예약 취소 처리
        reservationService.cancelReservation(reservation.getId());

        // 2. 좌석 상태 원복
        Seat seat = reservation.getSeat();
        seat.cancelStatus();
        seatService.save(seat);

        redisTemplate.opsForZSet().incrementScore("concertRanking", String.valueOf(reservation.getConcert().getId()), -1);

        System.out.println("예약 취소 완료, 예약ID=" + reservation.getId());
    }
}