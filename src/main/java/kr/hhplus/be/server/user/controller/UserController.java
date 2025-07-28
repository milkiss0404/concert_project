package kr.hhplus.be.server.user.controller;

import kr.hhplus.be.server.config.ui.Response;
import kr.hhplus.be.server.user.application.dto.ChargingCashRequest;
import kr.hhplus.be.server.user.application.dto.ChargingCashResponse;
import kr.hhplus.be.server.user.application.service.UserService;
import kr.hhplus.be.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/chargingCash")
    public Response<ChargingCashResponse>ChargingCash(@RequestBody ChargingCashRequest request) {
        User user = userService.chargingCash(request);
        return Response.ok(ChargingCashResponse.from(user));
    }
}
