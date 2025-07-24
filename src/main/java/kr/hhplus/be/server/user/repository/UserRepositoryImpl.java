package kr.hhplus.be.server.user.repository;

import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    @Override
    public User findById(Long userId) {
        UserEntity userEntity = jpaUserRepository.findById(userId).
                orElseThrow(() -> new IllegalArgumentException("없는 유저입니다"));
        return userEntity.toUser(userEntity);
    }
}
