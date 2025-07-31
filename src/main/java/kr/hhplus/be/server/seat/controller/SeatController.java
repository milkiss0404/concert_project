package kr.hhplus.be.server.seat.controller;

import kr.hhplus.be.server.config.ui.Response;
import kr.hhplus.be.server.seat.application.service.SeatService;
import kr.hhplus.be.server.seat.domain.ReservationStatus;
import kr.hhplus.be.server.seat.domain.Seat;
import kr.hhplus.be.server.seat.domain.Zone;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/seat")
public class SeatController {
    private final SeatService seatService;

    @GetMapping("/concert/status")
    public Response<List<Seat>> findSeatWhereConcertAndStatus(@RequestParam("concertId") Long concertId, @RequestParam("zone") Zone zone, @RequestParam("status") ReservationStatus status) {
        List<Seat> seatsByZoneAndConcertIdAndStatus = seatService.findSeatsByZoneAndConcertIdAndStatus(concertId, zone, status);
        return Response.ok(seatsByZoneAndConcertIdAndStatus);
    }
}
