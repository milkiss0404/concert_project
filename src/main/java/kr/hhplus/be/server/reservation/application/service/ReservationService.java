package kr.hhplus.be.server.reservation.application.service;

import kr.hhplus.be.server.concert.application.dtos.ConcertRankingCounterEvent;
import kr.hhplus.be.server.reservation.domain.Reservation;
import kr.hhplus.be.server.reservation.modelmapper.ReservationModelMapper;
import kr.hhplus.be.server.reservation.repository.ReservationRepository;
import kr.hhplus.be.server.reservation.repository.entity.ReservationEntity;
import kr.hhplus.be.server.user.application.dto.UsingPointEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationModelMapper reservationModelMapper;
    private final ApplicationEventPublisher eventPublisher;

    public void reserve(Reservation reservation) {
        reservationRepository.save(reservationModelMapper.toEntity(reservation));

        eventPublisher.publishEvent(new ConcertRankingCounterEvent(reservation.getConcertId()));
        eventPublisher.publishEvent(new UsingPointEvent(reservation.getUser(), reservation.getSeatPrice(),reservation));

    }

    public void cancelReservation(Long reservationId) {
        ReservationEntity reservationEntity = reservationRepository.findById(reservationId);
        Reservation reservation = reservationModelMapper.toDomain(reservationEntity);
        reservation.cancel();
        reserve(reservation);
    }
}
