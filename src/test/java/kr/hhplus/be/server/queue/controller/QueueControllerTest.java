package kr.hhplus.be.server.queue.controller;

import kr.hhplus.be.server.config.ui.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@DisplayName("레디스 대기열 진입")
class QueueControllerIntegrationTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private QueueController queueController;

    private final Long concertId = 100L;
    private final Long userId = 200L;

    private static final String QUEUE_KEY_PREFIX = "concert:queue:";

    @BeforeEach
    void cleanUp() {
        // 테스트 전 Redis 초기화
        redisTemplate.delete(QUEUE_KEY_PREFIX + concertId);
    }

    @Test
    @DisplayName("대기열 진입 API 호출 시 대기열에 userId 추가 및 토큰 반환 테스트")
    void testEnterQueue() {
        // 실제 API 호출 (컨트롤러 메서드 직접 호출)
        ResponseEntity<String> response = queueController.enterQueue(concertId, userId);

        // 응답 상태 확인
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // 토큰이 반환되었는지 확인
        String token = response.getBody();
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // Redis 리스트에 userId가 들어갔는지 확인
        String queueKey = QUEUE_KEY_PREFIX + concertId;
        List<String> queueList = redisTemplate.opsForList().range(queueKey, 0, -1); // 0 to -1 은 전체조회
        assertNotNull(queueList);
        assertTrue(queueList.contains(userId.toString()));

        // Redis에 토큰 키가 존재하는지 확인 (TTL 포함)
        String redisTokenKey = "queue:token:" + token;
        Boolean hasKey = redisTemplate.hasKey(redisTokenKey);
        assertTrue(Boolean.TRUE.equals(hasKey));

        // (선택) Pub/Sub 메시지 테스트는 별도 Listener를 통해 검증 가능
    }
}
