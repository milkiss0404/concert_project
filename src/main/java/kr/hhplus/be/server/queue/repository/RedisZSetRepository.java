package kr.hhplus.be.server.queue.repository;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Set;

public interface RedisZSetRepository {
    void enterQueue(Long concertId, Long userId);

    void saveToken(String token, Long concertId, Long userId);

    Long getRank(Long concertId , Long userId);

    Set<ZSetOperations.TypedTuple<String>> getQueue(Long concertId);

    Long dequeue(Long concertId);

    Long getQueueSize(Long concertId);

    void publishJoinEvent(Long userId, Long concertId);

    void RefreshCounter(Long concertId);

    void leaveQueue(Long concertId, Long userId);

    void startConcert(Long concertId);

    void endConcert(Long concertId);

    List<String> getTopConcerts(int topN);

    Long getConcertRank(Long concertId);
}
