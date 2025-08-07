package kr.hhplus.be.server.concert.application.userCase;

import kr.hhplus.be.server.concert.application.dtos.ConcertFindRequest;
import kr.hhplus.be.server.concert.application.dtos.ConcertSeatInfoRequest;
import kr.hhplus.be.server.concert.application.service.ConcertService;
import kr.hhplus.be.server.concert.modelMapper.ConcertModelMapper;
import kr.hhplus.be.server.concert.repository.entity.ConcertEntity;
import kr.hhplus.be.server.reservation.application.service.ReservationService;
import kr.hhplus.be.server.reservation.domain.Reservation;
import kr.hhplus.be.server.seat.application.service.SeatService;
import kr.hhplus.be.server.seat.modelMapper.SeatModelMapper;
import kr.hhplus.be.server.seat.repository.entity.SeatEntity;
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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Level;

@Component
@RequiredArgsConstructor
@Transactional
public class ReserveUseCase {
    private final ReservationService reservationService;
    private final ConcertService concertService;
    private final SeatService seatService;
    private final SeatRepository seatRepository;
    private final UserService userService;

    private final SeatModelMapper seatModelMapper;

    public Seat choiceSeatAndReserve(Long concertId, Long seatId,Long userId) {
            Concert concert = concertService.findConcert((concertId));
            Seat seat = seatService.seatInfo(concertId, seatId);
            User user = userService.findById(userId);

            Reservation reservation = Reservation.create(user, seat, concert);

            userService.usingUserPoint(user,seat.getSeatPrice());

            reservationService.reserve(reservation);

            seat.reserveStatus();
            seatRepository.save(seatModelMapper.toEntity(seat));
            return seat;
    }
}

