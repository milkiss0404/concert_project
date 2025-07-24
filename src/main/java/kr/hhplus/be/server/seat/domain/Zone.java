package kr.hhplus.be.server.seat.domain;

import lombok.Getter;

@Getter
public enum Zone {
    VIP(150_000),
    R(100_000),
    S(70_000),
    A(50_000);

    private final int price;

    Zone(int price) {
        this.price = price;
    }
}

