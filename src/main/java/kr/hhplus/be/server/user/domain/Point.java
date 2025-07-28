package kr.hhplus.be.server.user.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Point {
    private Long userId;
    private int point;

    public Point(Long userId, int point) {
        this.userId = userId;
        this.point = point;
    }

    public Point chargePoint(int amount) {
        return new Point(userId, point + amount);
    }
    public Point usePoint(int amount) {
        return new Point(userId, point - amount);
    }

}
