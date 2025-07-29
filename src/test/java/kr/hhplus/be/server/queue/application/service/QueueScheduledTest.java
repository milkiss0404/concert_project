package kr.hhplus.be.server.queue.application.service;

import kr.hhplus.be.server.queue.jwt.QueueTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@DisplayName("스케쥴러 테스트")
class QueueScheduledTest {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    QueueTokenProvider queueTokenProvider;

    @Autowired
    QueueService queueService;

    @Autowired
    QueueScheduled queueScheduled;


    @BeforeEach
    void setUp() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    @DisplayName("대기열_입장_허용_스케줄러_테스트")
    void 대기열_입장_허용_스케줄러_테스트() {
        // given
        Long concertId = 100L;
        Long userId = 1L;

        String queueKey = "concert:queue:" + concertId;
        String token = queueTokenProvider.createQueueToken(userId, concertId);
        String tokenKey = "queue:token:" + token;

        // 대기열에 사용자 추가
        redisTemplate.opsForList().rightPush(queueKey, userId.toString());
        // Redis에 토큰 저장
        redisTemplate.opsForValue().set(tokenKey, userId.toString());

        // when
        queueScheduled.allowEntranceIfValidToken(concertId);

        // then
        // 1. 대기열에서 사용자 제거됨
        Long queueSize = redisTemplate.opsForList().size(queueKey);
        assertThat(queueSize).isEqualTo(0);

        // 2. 토큰 삭제됨
        Set<String> keys = redisTemplate.keys("queue:token:*");
        assertThat(keys).isEmpty();
    }
}
