package kr.hhplus.be.server.user.repository.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.user.domain.Cash;
import kr.hhplus.be.server.user.domain.User;
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
    private Cash cash;

    public void updateCash(int cash) {
        this.cash = new Cash(id, cash);
    }

    public UserEntity(Cash cash) {
        this.cash = cash;
    }

    public int getCash() {
        return this.cash.getCash();
    }
}
