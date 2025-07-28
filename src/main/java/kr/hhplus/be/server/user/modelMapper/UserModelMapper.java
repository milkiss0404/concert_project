package kr.hhplus.be.server.user.modelMapper;

import kr.hhplus.be.server.user.domain.Cash;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserModelMapper {
    private final ModelMapper modelMapper;

    public UserEntity toEntity(User domain) {
        return modelMapper.map(domain, UserEntity.class);
    }
    public User toDomain(UserEntity entity) {
        return modelMapper.map(entity, User.class);
    }
    public User toDomainBuilder(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getPassWd(),
                new Cash(entity.getId(), entity.getCash())  // 또는 적절한 필드에서 cash 값을 추출
        );
    }
}
