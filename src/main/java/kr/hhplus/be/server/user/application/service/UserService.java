package kr.hhplus.be.server.user.application.service;

import kr.hhplus.be.server.user.application.dto.ChargingCashRequest;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.modelMapper.UserModelMapper;
import kr.hhplus.be.server.user.repository.UserRepository;
import kr.hhplus.be.server.user.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserModelMapper modelMapper;

    @Transactional
    public User chargingCash(ChargingCashRequest request) {
        UserEntity entity = userRepository.findById(request.userId());
        User user = modelMapper.toDomainBuilder(entity);
        User changed = user.chargeCash(request.amount());

        entity.updateCash(changed.getCash());
        return changed;
    }
}
