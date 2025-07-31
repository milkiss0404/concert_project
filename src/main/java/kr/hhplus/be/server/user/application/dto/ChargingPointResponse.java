package kr.hhplus.be.server.user.application.dto;

import kr.hhplus.be.server.user.domain.Point;
import kr.hhplus.be.server.user.domain.User;

public record ChargingPointResponse(
        Long id,
        String username,
        String passWd,
        Point point
){
    public static ChargingPointResponse from(User user) {
     return new ChargingPointResponse(
                user.getId(),
                user.getUsername(),
                user.getPassWd(),
                new Point(user.getPoint())
        );
    }
}
