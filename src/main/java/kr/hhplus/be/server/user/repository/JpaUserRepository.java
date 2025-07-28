package kr.hhplus.be.server.user.repository;

import kr.hhplus.be.server.user.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<UserEntity,Long> {
    Long id(Long id);
}
