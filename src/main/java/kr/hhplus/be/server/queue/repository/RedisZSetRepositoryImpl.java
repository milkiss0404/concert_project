package kr.hhplus.be.server.queue.repository;

import kr.hhplus.be.server.queue.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class RedisZSetRepositoryImpl implements RedisZSetRepository{

    private static final String TOKEN_KEY_PREFIX = "queue:token:";
    private final StringRedisTemplate redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Long getQueueSize(Long concertId) {
        return redisTemplate.opsForZSet().zCard("concertQueue:" + concertId);
    }

    @Override
    public Long dequeue(Long concertId) {
        ZSetOperations.TypedTuple<String> tuple = redisTemplate.opsForZSet().popMin("concertQueue:" + concertId);
        return tuple != null ? Long.valueOf(tuple.getValue()) : null;
    }

    @Override
    public Set<ZSetOperations.TypedTuple<String>> getQueue(Long concertId) {
        return redisTemplate.opsForZSet().rangeWithScores("concertQueue:" + concertId, 0, -1);
    }

    @Override
    public Long getRank(Long concertId , Long userId) {
        return redisTemplate.opsForZSet().rank("concertQueue:" + concertId, userId);
    }

    @Override
    public void saveToken(String token, Long concertId, Long userId) {
            String tokenKey = TOKEN_KEY_PREFIX + token;
            redisTemplate.opsForValue().set(tokenKey, userId.toString(), Duration.ofMinutes(60));
    }

    @Override
    public void enterQueue(Long concertId, Long userId) {
        String counterKey = "concertQueueCounter:" + concertId;
        Long order = redisTemplate.opsForValue().increment(counterKey, 1);
        redisTemplate.opsForZSet().add("concertQueue:" + concertId, userId.toString(), order);
        }
    @Override
    public void publishJoinEvent(Long userId, Long concertId) {
        redisTemplate.convertAndSend("queue:log", "User " + userId + " joined Queue " + concertId);
    }

    @Override
    public void RefreshCounter(Long concertId) {
        redisTemplate.opsForValue().set("concertQueueCounter:" + concertId, "0");
    }

    @Override
    public void leaveQueue(Long concertId, Long userId) {
        redisTemplate.opsForZSet().remove("concertQueue:" + concertId, userId.toString());
    }

    @Override
    public void startConcert(Long concertId) {
        redisTemplate.opsForSet().add("activeConcerts", concertId.toString());
    }

    @Override
    public void endConcert(Long concertId) {
        redisTemplate.opsForSet().remove("activeConcerts", concertId.toString());
    }
}
