package kr.hhplus.be.server.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("유저 테스트")
class UserTest {

    Cash cash;
    User user;

    @BeforeEach
    void setUp() {
        cash = new Cash(1L, 0);
        user = User.builder()
                .id(1L)
                .passWd("1234")
                .username("1234")
                .cash(cash)
                .build();
    }

    @Nested
    @DisplayName("캐시 충전 기능 테스트")
    class ChargeCashTest {

        @Test
        @DisplayName("캐시를 정상적으로 충전한다")
        void chargeCash() {
            // given
            // when
            User chargedUser = user.chargeCash(1000);

            // then
            assertEquals(1000, chargedUser.getCash());
        }

        @Test
        @DisplayName("0 이하 금액은 충전 시 예외가 발생한다")
        void chargeCashWithInvalidAmount() {
            // given
            // when & then
            assertThrows(IllegalArgumentException.class, () -> {
                user.chargeCash(0);
            });
        }
    }
    @DisplayName("캐시 사용테스트")
    @Nested
    class userCash{
        @Test
        @DisplayName("캐시 정상사용")
        void useCash() {
            // given
            User user1 = user.chargeCash(1000);
            // when
            user1.useCash(1000);
            // then
            assertEquals(0,user.getCash());
        }
        @Test
        @DisplayName("캐시 잔액 부족")
        void useCashWithInvalidAmount() {
            // given
            // when
            // then
            assertThrows(IllegalArgumentException.class, () -> {
                user.useCash(1000);
            });
        }

    }
}