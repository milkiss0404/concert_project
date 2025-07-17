package kr.hhplus.be.server.User.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/waiting-queue")
@Tag(name = "대기열", description = "대기열 토큰 발급 및 관리 Mock API")
public class WaitingQueueController {

    private static final int MAX_CONCURRENT = 2;

    private int currentConcurrentUsers = 3;

    @GetMapping("/check")
    @Operation(summary = "동시 접속자 수 확인 및 대기열 진입 판단(Mock)")
    public ResponseEntity<Map<String, Object>> checkQueue() {
        boolean enterQueue = currentConcurrentUsers > MAX_CONCURRENT;

        Map<String, Object> response = new HashMap<>();
        response.put("enterQueue", enterQueue);

        if (enterQueue) {
            response.put("queueToken", "mock-queue-token-1234");
            response.put("message", "대기열 토큰이 발급되었습니다.");
        } else {
            response.put("message", "바로 예약 요청 가능합니다.");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/release")
    @Operation(summary = "대기열에서 빠져나와 예약 요청(Mock)")
    public ResponseEntity<String> releaseFromQueue() {
        return ResponseEntity.ok("대기열에서 빠져나와 예약 요청 진행");
    }
}