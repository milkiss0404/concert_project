package kr.hhplus.be.server.User.repository.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.User.domain.Cash;

@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String passWd;
    @Embedded
    private Cash cash;
    
}
