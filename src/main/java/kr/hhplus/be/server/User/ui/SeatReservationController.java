package kr.hhplus.be.server.User.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seats")
@Tag(name = "좌석예약", description = "좌석 예약 및 결제 Mock API")
public class SeatReservationController {

    @PostMapping("/reserve")
    @Operation(summary = "좌석 예약 요청(Mock)")
    public ResponseEntity<String> reserveSeat() {
        return ResponseEntity.ok("좌석 A1 예약 요청 성공");
    }
}