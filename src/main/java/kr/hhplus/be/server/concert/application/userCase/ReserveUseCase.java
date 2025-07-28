package kr.hhplus.be.server.concert.application.userCase;

import kr.hhplus.be.server.reservation.domain.Reservation;
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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ReserveUseCase {
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;
    private final ConcertRepository concertRepository;
    private final UserRepository userRepository;
    private final UserModelMapper userModelMapper;
    private final UserService userService;


    public Seat choiceSeatAndReserve(Long concertId, ChoiceSeatRequest choiceSeatRequest) {
            Concert concert = concertRepository.findById(concertId);
            Seat seat = seatRepository.seatInfo(concertId, choiceSeatRequest.seatId());
            UserEntity userEntity = userRepository.findById(choiceSeatRequest.userId());
            User user = userModelMapper.toDomain(userEntity);

            seat.holdingStatus(); // holding 처리
            Reservation reservation = Reservation.create(user, seat, concert);
            //여기에 돈 사용하는 로직 추가
            userService.usingUserPoint(new UsingUserPointRequest(user.getId(), seat.getSeatPrice()));

            reservationRepository.reserve(reservation);
            seatRepository.save(seat);
            return seat;
    }
}

