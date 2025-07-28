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
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String passWd;
    @Embedded
    private Point point;

    public void updatePoint(int point) {
        this.point = new Point(id, point);
    }

    public UserEntity(Point point) {
        this.point = point;
    }

    public int getPoint() {
        return this.point.getPoint();
    }
}
