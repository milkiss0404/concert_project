package kr.hhplus.be.server.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("유저 테스트")
class UserTest {

    Point point;
    User user;

    @BeforeEach
    void setUp() {
        point = new Point(0);
        user = User.builder()
                .id(1L)
                .passWd("1234")
                .username("1234")
                .point(point)
                .build();
    }

    @Nested
    @DisplayName("캐시 충전 기능 테스트")
    class ChargePointTest {

        @Test
        @DisplayName("캐시를 정상적으로 충전한다")
        void chargePoint() {
            // given
            // when
            User chargedUser = user.chargePoint(1000);

            // then
            assertEquals(1000, chargedUser.getPoint());
        }

        @Test
        @DisplayName("0 이하 금액은 충전 시 예외가 발생한다")
        void chargePointWithInvalidAmount() {
            // given
            // when & then
            assertThrows(IllegalArgumentException.class, () -> {
                user.chargePoint(0);
            });
        }
    }
    @DisplayName("캐시 사용테스트")
    @Nested
    class userPoint {
        @Test
        @DisplayName("캐시 정상사용")
        void usePoint() {
            // given
            User user1 = user.chargePoint(1000);
            // when
            user1.usePoint(1000);
            // then
            assertEquals(0,user.getPoint());
        }
        @Test
        @DisplayName("캐시 잔액 부족")
        void usePointWithInvalidAmount() {
            // given
            // when
            // then
            assertThrows(IllegalArgumentException.class, () -> {
                user.usePoint(1000);
            });
        }

    }
}