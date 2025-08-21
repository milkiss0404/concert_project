package kr.hhplus.be.server.user.controller;

import kr.hhplus.be.server.config.ui.Response;
import kr.hhplus.be.server.user.application.dto.ChargingPointRequest;
import kr.hhplus.be.server.user.application.dto.ChargingPointResponse;
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

    @PostMapping("/chargingPoint")
    public Response<ChargingPointResponse>ChargingPoint(@RequestBody ChargingPointRequest request) {
        User user = userService.chargingPoint(request);
        return Response.ok(ChargingPointResponse.from(user));
    }
}
