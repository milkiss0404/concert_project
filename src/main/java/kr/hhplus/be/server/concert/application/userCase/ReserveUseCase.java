package kr.hhplus.be.server.concert.application.userCase;

import kr.hhplus.be.server.reservation.domain.Reservation;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.repository.UserRepository;
import kr.hhplus.be.server.concert.application.dtos.ChoiceSeatRequest;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.repository.ConcertRepository;
import kr.hhplus.be.server.reservation.repository.ReservationRepository;
import kr.hhplus.be.server.seat.domain.Seat;
import kr.hhplus.be.server.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReserveUseCase {
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;
    private final ConcertRepository concertRepository;
    private final UserRepository userRepository;

    @Transactional
    public Seat choiceSeat(Long concertId, ChoiceSeatRequest choiceSeatRequest) {
            Concert concert = concertRepository.findById(concertId);
            Seat seat = seatRepository.seatInfo(concertId, choiceSeatRequest.seatId());
            User user = userRepository.findById(choiceSeatRequest.userId());

            seat.holdingStatus(); // holding 처리
            Reservation reservation = Reservation.create(user, seat, concert);

            reservationRepository.reserve(reservation);
            seatRepository.save(seat);
            return seat;

    }
}

