package kr.hhplus.be.server.user.application.service;

import kr.hhplus.be.server.user.application.dto.ChargingCashRequest;
import kr.hhplus.be.server.user.domain.Cash;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.modelMapper.UserModelMapper;
import kr.hhplus.be.server.user.repository.UserRepository;
import kr.hhplus.be.server.user.repository.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
        ChargingCashRequest request = new ChargingCashRequest(userId, chargingAmount);

        when(userRepository.findById(userId)).thenReturn(null);

        // when & then
        assertThrows(NullPointerException.class, () -> {
            userService.chargingCash(request);
        });
    }

    @Test
    @DisplayName("충전금액이 0 원 인경우")
    void 충전금액이_0원인_경우(){
        //given
        Long userId = 1L;
        int chargingAmount = 0;
        int initialCashAmount = 20000;

        Cash initialCash = new Cash(userId, initialCashAmount);
        UserEntity userEntity = new UserEntity(initialCash);
        User user = new User(initialCash);

        ChargingCashRequest request = new ChargingCashRequest(userId, chargingAmount);

        when(userRepository.findById(userId)).thenReturn(userEntity);
        when(modelMapper.toDomainBuilder(any(UserEntity.class))).thenReturn(user);

        //when
        //then
        assertThrows( IllegalArgumentException.class,()->{
            userService.chargingCash(request);
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

        Cash initialCash = new Cash(userId, initialCashAmount);
        UserEntity userEntity = new UserEntity(initialCash);
        User user = new User(initialCash);

        ChargingCashRequest request = new ChargingCashRequest(userId, chargingAmount);

        when(userRepository.findById(userId)).thenReturn(userEntity);
        when(modelMapper.toDomainBuilder(any(UserEntity.class))).thenReturn(user);

        User result = userService.chargingCash(request);

        assertThat(result).isNotNull();
        assertThat(result.getCash()).isEqualTo(expectedCashAmount);

        assertThat(userEntity.getCash()).isEqualTo(expectedCashAmount);


        verify(userRepository).findById(userId);
        verify(modelMapper).toDomainBuilder(any(UserEntity.class));
    }
}