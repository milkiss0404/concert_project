package kr.hhplus.be.server.user.application.dto;

public record ChargingPointRequest(Long userId,
                                   int amount) {
}