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

    public UserEntity(Long i) {
    }

    public User toUser(UserEntity userEntity) {
        return User.builder()
                .id(id)
                .username(username)
                .passWd(passWd)
                .cash(cash)
                .build();
    }
}
