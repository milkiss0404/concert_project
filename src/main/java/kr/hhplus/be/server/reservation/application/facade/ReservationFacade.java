package kr.hhplus.be.server.reservation.application.facade;

import kr.hhplus.be.server.reservation.application.userCase.ReserveUseCase;
import kr.hhplus.be.server.queue.redis.DistributedLock;
import kr.hhplus.be.server.seat.application.service.SeatService;
import kr.hhplus.be.server.seat.domain.Seat;
import kr.hhplus.be.server.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
/**
 * 파사드는 외부(Controller 등)의 요청을 받아 락킹을 걸고,
 * Use Case를 호출하며, 시스템 레벨의 예외 처리 및 보상 로직을 담당합니다.
 */
public class ReservationFacade {

    private final ReserveUseCase reserveUsecase; // Use Case 의존
    private final UserService userService;
    private final SeatService seatService;
    // 분산 락, 트랜잭션 시작/종료 경계, 예외 처리 담당
    @DistributedLock(key = "'concert:' + #concertId + ':seat:' + #seatId")
    public Seat choiceSeatAndReserveWithLock(Long concertId, Long seatId, Long userId) {
        Seat seat = null;
        boolean pointDeducted = false;

        try {
            // 핵심 비즈니스 로직은 Use Case에게 위임
            seat = reserveUsecase.choiceSeatAndReserve(concertId, seatId, userId);
            pointDeducted = true; // Use Case 내에서 예약 및 포인트 차감이 성공했다고 가정

            return seat;
        } catch (Exception e) {
            // 실패 시 보상 트랜잭션 실행 (Facade의 역할)
            compensateReservation(pointDeducted, seat, userId);
            throw new RuntimeException("예약 처리 중 시스템 오류 발생", e);
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
