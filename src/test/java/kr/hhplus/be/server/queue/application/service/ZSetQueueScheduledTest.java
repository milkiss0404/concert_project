package kr.hhplus.be.server.queue.application.service;

import kr.hhplus.be.server.config.CustomTestContainer;
import kr.hhplus.be.server.queue.jwt.JwtTokenProvider;
import kr.hhplus.be.server.queue.repository.RedisZSetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ZSet을 이용한 대기열")
class ZSetQueueScheduledTest extends CustomTestContainer {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ZSetQueueScheduled queueScheduled;

    @Autowired
    private RedisZSetRepository redisZSetRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("콘서트 활성화 ->콘서트 대기열 등록 -> 대기열 빠져나가기 -> pub/sub 으로 송/수신")
    @Test
    public void testProcessQueueWithRealRedis() {
        Long concertId = 1L;
        Long userId = 100L;

        // 1️⃣ 활성 콘서트 등록
        redisZSetRepository.startConcert(concertId);

        // 2️⃣ 대기열 등록 (Sorted Set)
        redisZSetRepository.enterQueue(concertId,userId);

        // 3️⃣ 토큰 등록: JWT 생성 후 실제 Bean 으로 검증 가능
        String testJwt = jwtTokenProvider.createJwtToken(100L, 1L); // 실제 JWT 생성
        redisTemplate.opsForValue().set("queue:token:" + testJwt, userId.toString());

        // 4️⃣ 스케줄러 호출
        queueScheduled.scheduledConcert();

        // 5️⃣ 결과 확인
        Set<ZSetOperations.TypedTuple<String>> remaining = redisTemplate.opsForZSet()
                .rangeWithScores("concertQueue:" + concertId, 0, -1);
        assertTrue(remaining == null || remaining.isEmpty(), "대기열에서 유저가 제거되어야 합니다");

        String tokenValue = redisTemplate.opsForValue().get("queue:token:" + testJwt);
        assertNull(tokenValue, "사용한 토큰은 삭제되어야 합니다");
    }
}