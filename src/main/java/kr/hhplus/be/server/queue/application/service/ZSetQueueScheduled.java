package kr.hhplus.be.server.queue.application.service;

import kr.hhplus.be.server.queue.jwt.JwtTokenProvider;
import kr.hhplus.be.server.queue.repository.RedisZSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@RequiredArgsConstructor
@Component
public class ZSetQueueScheduled {
    private final JwtTokenProvider queueTokenProvider;
    private final RedisZSetRepository redisZSetRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String TOKEN_KEY_PREFIX = "queue:token:";


    @Scheduled(fixedDelay = 3000)
    public void scheduledConcert() {
        Set<String> activeConcerts = redisTemplate.opsForSet().members("activeConcerts");
        if (activeConcerts != null) {
            for (String id : activeConcerts) {
                processQueue(Long.valueOf(id));
        }
    }
}

    private void processQueue(Long concertId) {
        //Sorted Set에서 가장 앞 유저 조회 (삭제 X)
        Set<ZSetOperations.TypedTuple<String>> front = redisTemplate.opsForZSet()
                .rangeWithScores("concertQueue:" + concertId, 0, 0);

        if (front == null || front.isEmpty()) return;

        ZSetOperations.TypedTuple<String> tuple = front.iterator().next();
        String userId = tuple.getValue();

        // 토큰 검증
        if (userId != null && processToken(userId, concertId)) {
            // 토큰 유효하면 Sorted Set에서 제거
            redisZSetRepository.leaveQueue(concertId, Long.valueOf(userId));
        }
    }
    private boolean processToken(String userId, Long concertId) {
        // Redis에서 토큰 조회
        Set<String> tokenKeys = redisTemplate.keys(TOKEN_KEY_PREFIX + "*");
        if (tokenKeys == null || tokenKeys.isEmpty()) return false;

        for (String key : tokenKeys) {
            String storedUserId = redisTemplate.opsForValue().get(key);
            if (userId.equals(storedUserId)) {
                String token = key.substring(TOKEN_KEY_PREFIX.length());
                if (queueTokenProvider.validateToken(token)) {
                    redisZSetRepository.publishJoinEvent(Long.valueOf(userId), concertId);
                    redisTemplate.delete(key);
                    return true;
                }
            }
        }
        return false;
    }
}

