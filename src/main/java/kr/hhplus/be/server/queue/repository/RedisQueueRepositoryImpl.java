package kr.hhplus.be.server.queue.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class RedisQueueRepositoryImpl implements QueueRepository {


    private final RedisTemplate<String, String> redisTemplate;

    private static final String QUEUE_KEY_PREFIX = "concert:queue:";
    private static final String TOKEN_KEY_PREFIX = "queue:token:";
    @Override
    public void publishJoinEvent(Long userId, Long concertId) {
        redisTemplate.convertAndSend("queue:log", "User " + userId + " joined Queue " + concertId);
    }
    @Override
    public void saveToken(String token, Long userId) {
        String tokenKey = TOKEN_KEY_PREFIX + token;
        redisTemplate.opsForValue().set(tokenKey, userId.toString(), Duration.ofMinutes(60));
    }

    @Override
    public void pushToQueue(Long concertId, Long userId) {
        String queueKey = QUEUE_KEY_PREFIX + concertId;
        redisTemplate.opsForList().rightPush(queueKey, userId.toString());
    }
}
