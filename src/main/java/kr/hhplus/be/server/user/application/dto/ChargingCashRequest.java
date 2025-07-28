package kr.hhplus.be.server.user.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.hhplus.be.server.user.domain.Cash;

public record ChargingCashRequest(  Long userId,
                                    int amount) {
}