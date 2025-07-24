package kr.hhplus.be.server.seat.domain;

import lombok.Getter;

@Getter
public enum SeatGrade {
    VIP(150_000),
    R(100_000),
    S(70_000),
    A(50_000);

    private final int price;

    SeatGrade(int price) {
        this.price = price;
    }
}
