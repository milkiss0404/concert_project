package kr.hhplus.be.server.user.application.dto;

import kr.hhplus.be.server.reservation.domain.Reservation;
import kr.hhplus.be.server.user.domain.User;

public record UsingPointEvent(User user , int price, Reservation reservation) {
}
