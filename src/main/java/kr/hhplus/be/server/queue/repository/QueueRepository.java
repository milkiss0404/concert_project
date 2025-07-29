package kr.hhplus.be.server.queue.repository;

public interface QueueRepository {

    void pushToQueue(Long concertId, Long userId);
    void saveToken(String token, Long userId);
    void publishJoinEvent(Long concertId,Long userId);
}
