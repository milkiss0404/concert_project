package kr.hhplus.be.server.user.repository;

import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.modelMapper.UserModelMapper;
import kr.hhplus.be.server.user.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;

    @Override
    public void save(UserEntity user) {
        jpaUserRepository.save(user);
    }

    @Override
    public UserEntity findById(Long userId) {
       return jpaUserRepository.findById(userId).
                orElseThrow(() -> new IllegalArgumentException("없는 유저입니다"));
    }


    @Override
    public UserEntity findByIdForLock(User user) {
        return jpaUserRepository.findByIdForUpdate(user.getId()).
                orElseThrow(() -> new IllegalArgumentException("없는 유저입니다"));
    }
}
