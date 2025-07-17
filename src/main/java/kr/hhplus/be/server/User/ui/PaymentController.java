package kr.hhplus.be.server.User.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@Tag(name = "결제", description = "결제 처리 Mock API")
public class PaymentController {

    @PostMapping("/pay")
    @Operation(summary = "결제 요청(Mock)")
    public ResponseEntity<String> processPayment() {
        return ResponseEntity.ok("결제 완료. 결제 금액: 2000");
    }
}