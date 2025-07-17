package kr.hhplus.be.server.User.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.User.application.dto.QueueEntryRequest;
import kr.hhplus.be.server.User.application.dto.QueueStatusResponse;
import kr.hhplus.be.server.User.domain.Cash;
import kr.hhplus.be.server.User.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/users")
@Tag(name = "User API", description = "유저 관련 Mock API")
public class UserController {

    @RestController
    @RequestMapping("/api/queue")
    @Tag(name = "대기열", description = "콘서트 대기열 Mock API")
    public class QueueController {

        @PostMapping("/enter")
        @Operation(summary = "대기열 진입 요청 (Mock)")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "대기열 진입 성공"),
                @ApiResponse(responseCode = "400", description = "잘못된 요청")
        })
        public ResponseEntity<QueueStatusResponse> enterQueue() {
            QueueStatusResponse response = new QueueStatusResponse(
                    "WAITING",
                    5,
                    "대기열에 성공적으로 진입했습니다. 현재 4명이 앞에 있습니다."
            );

            return ResponseEntity.ok(response);
        }

        @GetMapping("/status")
        @Operation(summary = "대기열 상태 조회 (Mock)")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "대기열 상태 조회 성공"),
                @ApiResponse(responseCode = "404", description = "대기열 상태 없음")
        })
        public ResponseEntity<QueueStatusResponse> getQueueStatus() {
            QueueStatusResponse response = new QueueStatusResponse(
                    "ENTERED",
                    1,
                    "곧 입장할 차례입니다."
            );

            return ResponseEntity.ok(response);
        }
    }
}
