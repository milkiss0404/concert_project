package kr.hhplus.be.server.queue.controller;

import kr.hhplus.be.server.config.CustomTestContainer;
import kr.hhplus.be.server.queue.jwt.JwtTokenProvider;
import kr.hhplus.be.server.queue.repository.RedisZSetRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ZsetController 테스트")
class ZSetQueueControllerTest extends CustomTestContainer {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RedisZSetRepository redisZSetRepository;

    @DisplayName("콘서트 시작 -> 유저큐입장 -> 퇴장 -> 콘서트종료 테스트 ")
    @Test
    void testEnterQueueAndTokenFlow() {
        Long concertId = 1L;
        Long userId = 100L;

        // 1️⃣ 콘서트 시작
        ResponseEntity<String> startResponse = restTemplate.postForEntity(
                "/" + concertId + "/start", null, String.class
        );
        assertEquals(HttpStatus.OK, startResponse.getStatusCode());

        // 2️⃣ 유저 큐 입장
        ResponseEntity<String> tokenResponse = restTemplate.postForEntity(
                "/" + concertId + "/queue/zset?userId=" + userId,
                null,
                String.class
        );
        assertEquals(HttpStatus.OK, tokenResponse.getStatusCode());

        String token = tokenResponse.getBody();
        assertNotNull(token, "JWT 토큰이 생성되어야 합니다");

        // 3️⃣ Redis에 토큰이 저장되었는지 확인
        String storedUserId = redisTemplate.opsForValue().get("queue:token:" + token);
        assertEquals(String.valueOf(userId), storedUserId);

        // 4️⃣ 콘서트 종료
        ResponseEntity<String> endResponse = restTemplate.postForEntity(
                "/" + concertId + "/end", null, String.class
        );
        assertEquals(HttpStatus.OK, endResponse.getStatusCode());
    }

    @DisplayName("incr 리셋")
    @Test
    void testCounterReset() {
        Long concertId = 1L;

        // Counter 초기값 세팅
        redisTemplate.opsForValue().set("concertQueueCounter:" + concertId, "10");

        // 리셋 호출
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/" + concertId + "/queue/zset/counterReset", null, String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // 값 확인
        String counter = redisTemplate.opsForValue().get("concertQueueCounter:" + concertId);
        assertEquals("0", counter, "카운터가 0으로 리셋되어야 합니다");
    }
}