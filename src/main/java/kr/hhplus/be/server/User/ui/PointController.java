package kr.hhplus.be.server.User.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/points")
@Tag(name = "포인트", description = "잔액 충전 및 조회 Mock API")
public class PointController {


    @GetMapping("/balance")
    @Operation(summary = "잔액 조회(Mock)")
    public ResponseEntity<String> getBalance() {
        return ResponseEntity.ok("포인트  잔액: 3000");
    }

    @PostMapping("/charge")
    @Operation(summary = "포인트 충전 요청(Mock)")
    public ResponseEntity<String> chargePoint() {
        return ResponseEntity.ok("포인트 3000 충전 완료. 잔액: 8000");
    }
}