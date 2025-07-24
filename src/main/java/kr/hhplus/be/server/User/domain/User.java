package kr.hhplus.be.server.User.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class User {
    private Long id;
    private String username;
    private String passWd;
    private Cash cash;

    public User chargeCash(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("충전금액은 0보다 커야합니다");
        }
        return new User(id, username, passWd, cash.chargeCash(amount));
    }
    public int getCash() {
        return cash.getCash();
    }

    public User useCash(int amount) {
        if (amount>this.cash.getCash()){
            throw new IllegalArgumentException("잔액이 부족합니다");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("사용금액은 0보다 커야합니다");
        }

        return new User(id, username, passWd, cash.useCash(amount));
    }
}
