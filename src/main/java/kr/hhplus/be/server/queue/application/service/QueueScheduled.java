package kr.hhplus.be.server.queue.application.service;

import kr.hhplus.be.server.queue.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@RequiredArgsConstructor
@Component
public class QueueScheduled {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider queueTokenProvider;
    private final QueueService queueService;

    private static final int MAX = 100;
    private static final String QUEUE_KEY_PREFIX = "concert:queue:";
    private static final String TOKEN_KEY_PREFIX = "queue:token:";

    @Scheduled(fixedDelay = 3000)
    public void allowEntranceIfValidToken(Long concertId) {
        String queueKey = QUEUE_KEY_PREFIX + concertId;
        Long currentQueueSize = redisTemplate.opsForList().size(queueKey);

        if (currentQueueSize != null && currentQueueSize > 0 && currentQueueSize <= MAX) {
            // 대기열에서 가장 앞 사람 꺼내기 (선입선출)
            String userId = redisTemplate.opsForList().leftPop(queueKey);
            if (userId == null) {
                return; // 대기열이 비어있으면 종료 TODO: 에러처리
            }

            // 토큰 키 패턴 조회
            Set<String> tokenKeys = redisTemplate.keys(TOKEN_KEY_PREFIX + "*");
            if (tokenKeys == null || tokenKeys.isEmpty()) {
                // TODO: 에러처리
                return;
            }

            // 해당 userId의 토큰 찾기
            for (String key : tokenKeys) {
                String storedUserId = redisTemplate.opsForValue().get(key);
                if (userId.equals(storedUserId)) {
                    // 키에서 토큰 부분만 추출
                    String token = key.substring(TOKEN_KEY_PREFIX.length());

                    // JWT 유효성 검사
                    if (queueTokenProvider.validateToken(token)) {
                        // 토큰이 유효하면 입장 허용 처리
                        redisTemplate.convertAndSend("queue:log", "User " + userId + " out the Queue " + concertId);
//                        queueService.allowEntrance(Long.parseLong(userId), concertId); :TODO: 입장하면 뭘할건지

                        redisTemplate.delete(key);
                    }
                    break;
                }
            }
        }
    }



}
