package kr.hhplus.be.server.queue.controller;

import kr.hhplus.be.server.queue.jwt.QueueTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/concert")
@RequiredArgsConstructor
public class QueueController {

    private final RedisTemplate<String, String> redisTemplate;
    private final QueueTokenProvider queueTokenProvider;

    private static final String QUEUE_KEY_PREFIX = "concert:queue:";
    private static final String TOKEN_KEY_PREFIX = "queue:token:";

    @PostMapping("/{concertId}/queue")
    public ResponseEntity<String> enterQueue(
            @PathVariable Long concertId,
            @RequestParam Long userId
    ) {
        String queueKey = QUEUE_KEY_PREFIX + concertId;

        // 1. 대기열에 userId 추가
        redisTemplate.opsForList().rightPush(queueKey, userId.toString());

        // 2. JWT 토큰 생성
        String token = queueTokenProvider.createQueueToken(userId, concertId);

        // 3. Redis에 토큰 저장 (TTL 60분)
        String redisTokenKey = TOKEN_KEY_PREFIX + token;
        redisTemplate.opsForValue().set(redisTokenKey, userId.toString(), Duration.ofMinutes(60));

        // 4. 대기열 진입 메시지 발행
        redisTemplate.convertAndSend("queue:log", "User " + userId + " joined Queue " + concertId);

        // 5. 토큰 응답
        return ResponseEntity.ok(token);
    }
}