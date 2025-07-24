package kr.hhplus.be.server.User.domain;

import jakarta.persistence.Embeddable;
import kr.hhplus.be.server.User.repository.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Cash {
    private Long userId;
    private int cash;

    public Cash(Long userId, int cash) {
        this.userId = userId;
        this.cash = cash;
    }

    public Cash chargeCash(int amount) {
        return new Cash(userId, cash + amount);
    }
    public Cash useCash(int amount) {
        return new Cash(userId, cash - amount);
    }

}
