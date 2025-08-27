package kr.hhplus.be.server.config.common.handler;

import kr.hhplus.be.server.concert.application.dtos.ConcertRankingCounterEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ConcertRankingCounterEventHandler {
    private final RedisTemplate<String, String> redisTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void concertRankingUp(ConcertRankingCounterEvent event){
            redisTemplate.opsForZSet().incrementScore("concertRanking", String.valueOf(event.concertId()), 1);
    }
}
