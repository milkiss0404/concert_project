package kr.hhplus.be.server.user.application.service;

import kr.hhplus.be.server.user.application.dto.ChargingPointRequest;
import kr.hhplus.be.server.user.application.dto.UsingUserPointRequest;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.modelMapper.UserModelMapper;
import kr.hhplus.be.server.user.repository.UserRepository;
import kr.hhplus.be.server.user.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserModelMapper userModelMapper;


    public User findById(Long userId) {
        UserEntity byId = userRepository.findById(userId);
        return userModelMapper.toDomain(byId);
    }

    public User chargingPoint(ChargingPointRequest request) {
        UserEntity entity = userRepository.findById(request.userId());
        User user = userModelMapper.toDomainBuilder(entity);
        User changed = user.chargePoint(request.amount());

        entity.updatePoint(changed.getPoint());
        return changed;
    }

    public User usingUserPoint(User user,  int price) {
        UserEntity userEntity = userRepository.findByIdForLock(user);
        userEntity.usingPoint(price);
        return userModelMapper.toDomain(userEntity);
    }
}
