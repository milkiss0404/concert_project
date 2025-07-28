package kr.hhplus.be.server.user.application.dto;

import kr.hhplus.be.server.user.domain.Cash;
import kr.hhplus.be.server.user.domain.User;

public record ChargingCashResponse (
        Long id,
        String username,
        String passWd,
        Cash cash
){
    public static ChargingCashResponse from(User user) {
     return new ChargingCashResponse(
                user.getId(),
                user.getUsername(),
                user.getPassWd(),
                new Cash(user.getId(), user.getCash())
        );
    }
}
