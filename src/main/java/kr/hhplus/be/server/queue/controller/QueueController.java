package kr.hhplus.be.server.queue.controller;

import kr.hhplus.be.server.queue.jwt.JwtTokenProvider;
import kr.hhplus.be.server.queue.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/concert")
@RequiredArgsConstructor
public class QueueController {

    private final QueueRepository queueRepository;
    private final JwtTokenProvider queueTokenProvider;

    @PostMapping("/{concertId}/queue")
    public ResponseEntity<String> enterQueue(
            @PathVariable Long concertId,
            @RequestParam Long userId
    ) {

        queueRepository.pushToQueue(concertId, userId);

        String token = queueTokenProvider.createJwtToken(userId, concertId);

        queueRepository.saveToken(token,userId);

        queueRepository.publishJoinEvent(concertId,userId);
        return ResponseEntity.ok(token);
    }
}