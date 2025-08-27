package kr.hhplus.be.server.reservation.application.dto;

import kr.hhplus.be.server.reservation.domain.Reservation;

public record CancelReservationEvent(Reservation reservation) {

}
