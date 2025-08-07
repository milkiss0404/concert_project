package kr.hhplus.be.server.user.repository.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.user.domain.Point;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String passWd;
    @Embedded
    private Point point;

    public void updatePoint(int point) {
        this.point = new Point(point);
    }

    public UserEntity(Point point) {
        this.point = point;
    }

    public int getPoint() {
        return this.point.getPoint();
    }

    public int usingPoint(int point) {
        int currentPoint = this.point.getPoint();

        if (point <= 0) {
            throw new IllegalArgumentException("사용금액은 0보다 커야합니다");
        }
        if (currentPoint < point) {
            throw new IllegalArgumentException("포인트가 부족합니다");
        }
       return (this.point = new Point(currentPoint - point)).getPoint();
    }
}
