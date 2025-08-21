package kr.hhplus.be.server.user.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Point {
    private int point;

    public Point(int point) {
        this.point = point;
    }

    public Point chargePoint(int amount) {
        return new Point(point + amount);
    }
    public Point usePoint(int amount) {
        return new Point (point - amount);
    }

}
