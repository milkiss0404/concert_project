package kr.hhplus.be.server.user.repository;

import kr.hhplus.be.server.user.domain.User;

public interface UserRepository {
    User findById(Long userId);
}
