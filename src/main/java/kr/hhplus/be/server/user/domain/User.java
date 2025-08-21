package kr.hhplus.be.server.user.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class User {
    private Long id;
    private String username;
    private String passWd;
    private Point point;

    public User(Long i) {
    }

    public User chargePoint(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("충전금액은 0보다 커야합니다");
        }
        return new User(id, username, passWd, point.chargePoint(amount));
    }
    public int getPoint() {
        return point.getPoint();
    }

    public User usePoint(int amount) {
        if (amount>this.point.getPoint()){
            throw new IllegalArgumentException("잔액이 부족합니다");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("사용금액은 0보다 커야합니다");
        }

        return new User(id, username, passWd, point.usePoint(amount));
    }

    public User(Point point) {
        this.point = point;
    }
}
