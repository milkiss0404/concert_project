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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class RedisZSetRepositoryImpl implements RedisZSetRepository{

    private static final String TOKEN_KEY_PREFIX = "queue:token:";
    private final StringRedisTemplate redisTemplate;

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

    @Override
    public List<String> getTopConcerts(int topN) {
        // score 기준 내림차순으로 상위 N개
        Set<String> topConcerts = redisTemplate.opsForZSet().reverseRange("concertRanking", 0, topN - 1);
        if (topConcerts == null) return Collections.emptyList();
        return new ArrayList<>(topConcerts);
    }
    @Override
    public Long getConcertRank(Long concertId) {
        // 내림차순 랭킹
        Long rank = redisTemplate.opsForZSet().reverseRank("concertRanking", String.valueOf(concertId));
        return rank != null ? rank + 1 : null; // 0부터 시작하므로 +1
    }
}
