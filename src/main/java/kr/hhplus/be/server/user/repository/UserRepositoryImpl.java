package kr.hhplus.be.server.user.repository;

import kr.hhplus.be.server.user.domain.Cash;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.modelMapper.UserModelMapper;
import kr.hhplus.be.server.user.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    private final UserModelMapper modelMapper;

    @Override
    public void save(UserEntity user) {
        jpaUserRepository.save(user);
    }

    @Override
    public UserEntity findById(Long userId) {
       return jpaUserRepository.findById(userId).
                orElseThrow(() -> new IllegalArgumentException("없는 유저입니다"));
    }
}
