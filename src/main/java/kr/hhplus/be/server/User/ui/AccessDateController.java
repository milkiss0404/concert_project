package kr.hhplus.be.server.User.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/access-dates")
@Tag(name = "예약가능날짜", description = "예약 가능 날짜 및 좌석 Mock API")
public class AccessDateController {

    @GetMapping
    @Operation(summary = "예약 가능한 날짜 조회(Mock)")
    public ResponseEntity<List<String>> getAvailableDates() {
        List<String> dates = List.of("2025-07-20", "2025-07-21", "2025-07-22");
        return ResponseEntity.ok(dates);
    }

    @GetMapping("/seats")
    @Operation(summary = "예약 가능한 좌석 목록 조회(Mock)")
    public ResponseEntity<List<String>> getAvailableSeats() {
        List<String> seats = List.of("A1", "A2", "B1", "B2");
        return ResponseEntity.ok(seats);
    }
}