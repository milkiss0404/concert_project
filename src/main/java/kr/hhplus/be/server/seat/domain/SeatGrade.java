package kr.hhplus.be.server.seat.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeatGrade {
    VIP(150_000),
    R(100_000),
    S(70_000),
    A(50_000);

    private final int price;

}
