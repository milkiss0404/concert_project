package kr.hhplus.be.server.reservation.application.service;

import kr.hhplus.be.server.reservation.domain.Reservation;
import kr.hhplus.be.server.reservation.modelmapper.ReservationModelMapper;
import kr.hhplus.be.server.reservation.repository.ReservationRepository;
import kr.hhplus.be.server.reservation.repository.entity.ReservationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationModelMapper reservationModelMapper;

    public void reserve(Reservation reservation) {
        reservationRepository.save(reservationModelMapper.toEntity(reservation));
    }
}
