package kr.hhplus.be.server.user.repository;

import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.repository.entity.UserEntity;

public interface UserRepository {
    UserEntity findById(Long userId);

   void save(UserEntity userEntity);

    UserEntity findByIdForLock(User user);
}
