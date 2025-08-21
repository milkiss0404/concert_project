package kr.hhplus.be.server.queue.controller;

import kr.hhplus.be.server.config.ui.Response;
import kr.hhplus.be.server.queue.jwt.JwtTokenProvider;
import kr.hhplus.be.server.queue.repository.QueueRepository;
import kr.hhplus.be.server.queue.repository.RedisZSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ZSetQueueController {
    private final RedisZSetRepository redisZSetRepository;
    private final JwtTokenProvider queueTokenProvider;

    @PostMapping("/{concertId}/queue/zset")
    public ResponseEntity<String> enterQueue(
            @PathVariable Long concertId,
            @RequestParam Long userId
    ) {

        redisZSetRepository.enterQueue(concertId, userId);

        String token = queueTokenProvider.createJwtToken(userId, concertId);

        redisZSetRepository.saveToken(token,concertId,userId);

        redisZSetRepository.publishJoinEvent(concertId,userId);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/{concertId}/start")
    public Response<?> startConcert(@PathVariable Long concertId) {
        redisZSetRepository.startConcert(concertId);
        return Response.ok(null);

    }
    @PostMapping("/{concertId}/end")
    public Response<?> endConcert(@PathVariable Long concertId) {
        redisZSetRepository.endConcert(concertId);
        return Response.ok(null);

    }

    @PostMapping("{concertId}/queue/zset/counterReset")
    public Response<?> counterReset(@PathVariable Long concertId){
        redisZSetRepository.RefreshCounter(concertId);
        return Response.ok(null);
    }
}
