package kr.hhplus.be.server.user.application.service;

import kr.hhplus.be.server.user.application.dto.ChargingPointRequest;
import kr.hhplus.be.server.user.domain.Point;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.modelMapper.UserModelMapper;
import kr.hhplus.be.server.user.repository.UserRepository;
import kr.hhplus.be.server.user.repository.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@DisplayName("유저 포인트 충전/사용 ")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("사용자를 찾을 수 없는 경우")
    void 사용자를_찾을_수_없는_경우() {
        // given
        Long userId = 999L;
        int chargingAmount = 10000;
        ChargingPointRequest request = new ChargingPointRequest(userId, chargingAmount);

        when(userRepository.findById(userId)).thenReturn(null);

        // when & then
        assertThrows(NullPointerException.class, () -> {
            userService.chargingPoint(request);
        });
    }

    @Test
    @DisplayName("충전금액이 0 원 인경우")
    void 충전금액이_0원인_경우(){
        //given
        Long userId = 1L;
        int chargingAmount = 0;
        int initialCashAmount = 20000;

        Point initialPoint = new Point(userId, initialCashAmount);
        UserEntity userEntity = new UserEntity(initialPoint);
        User user = new User(initialPoint);

        ChargingPointRequest request = new ChargingPointRequest(userId, chargingAmount);

        when(userRepository.findById(userId)).thenReturn(userEntity);
        when(modelMapper.toDomainBuilder(any(UserEntity.class))).thenReturn(user);

        //when
        //then
        assertThrows( IllegalArgumentException.class,()->{
            userService.chargingPoint(request);
                });

        verify(userRepository).findById(userId);
        verify(modelMapper).toDomainBuilder(any(UserEntity.class));
    }

    @Test
    @DisplayName("사용자 캐시 충전 성공")
    void 사용자_캐시_충전_성공() {

        // given
        Long userId = 1L;
        int chargingAmount = 10000;
        int initialCashAmount = 20000;
        int expectedCashAmount = 30000;

        Point initialPoint = new Point(userId, initialCashAmount);
        UserEntity userEntity = new UserEntity(initialPoint);
        User user = new User(initialPoint);

        ChargingPointRequest request = new ChargingPointRequest(userId, chargingAmount);

        when(userRepository.findById(userId)).thenReturn(userEntity);
        when(modelMapper.toDomainBuilder(any(UserEntity.class))).thenReturn(user);

        User result = userService.chargingPoint(request);

        assertThat(result).isNotNull();
        assertThat(result.getPoint()).isEqualTo(expectedCashAmount);

        assertThat(userEntity.getPoint()).isEqualTo(expectedCashAmount);


        verify(userRepository).findById(userId);
        verify(modelMapper).toDomainBuilder(any(UserEntity.class));
    }
}