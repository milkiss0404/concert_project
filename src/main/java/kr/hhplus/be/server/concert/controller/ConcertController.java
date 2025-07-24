package kr.hhplus.be.server.concert.controller;

import kr.hhplus.be.server.concert.application.dtos.ConcertFindRequest;
import kr.hhplus.be.server.concert.application.dtos.ConcertFindResponse;
import kr.hhplus.be.server.concert.application.dtos.ConcertSeatInfoResponse;
import kr.hhplus.be.server.concert.application.dtos.ConcertSeatInfoRequest;
import kr.hhplus.be.server.concert.application.service.ConcertService;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.config.ui.Response;
import kr.hhplus.be.server.seat.application.service.SeatService;
import kr.hhplus.be.server.seat.domain.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/concert")
@RestController
@RequiredArgsConstructor
public class ConcertController {
    private final ConcertService concertService;
    private final SeatService seatService;

    @GetMapping("/find")
    public Response<ConcertFindResponse> findConcert(ConcertFindRequest concertFindRequest) {
        Concert concert = concertService.findConcert(concertFindRequest);
        ConcertFindResponse response = ConcertFindResponse.from(concert);
        return Response.ok(response);
    }

    @GetMapping("/{concertId}/seats")
    public Response<ConcertSeatInfoResponse> seatInfo(@PathVariable("concertId")Long concertId, ConcertSeatInfoRequest seatInfoRequest) {
        Seat seat = seatService.seatInfo(concertId, seatInfoRequest);
        ConcertSeatInfoResponse response = ConcertSeatInfoResponse.from(seat);
        return Response.ok(response);
    }
}
