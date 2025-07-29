package kr.hhplus.be.server.concert.controller;

import kr.hhplus.be.server.concert.application.dtos.*;
import kr.hhplus.be.server.concert.application.service.ConcertService;
import kr.hhplus.be.server.concert.application.userCase.ReserveUseCase;
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
    private final ReserveUseCase reserveUseCase;

    //콘서트 조회
    @GetMapping("/find")
    public Response<ConcertFindResponse> findConcert(@RequestBody ConcertFindRequest concertFindRequest) {
        Concert concert = concertService.findConcert(concertFindRequest);
        ConcertFindResponse response = ConcertFindResponse.from(concert);
        return Response.ok(response);
    }

    //콘서트 자리 조회
    @GetMapping("/{concertId}/seats")
    public Response<ConcertSeatInfoResponse> seatInfo(@PathVariable("concertId")Long concertId, ConcertSeatInfoRequest seatInfoRequest) {
        Seat seat = seatService.seatInfo(concertId, seatInfoRequest);
        ConcertSeatInfoResponse response = ConcertSeatInfoResponse.from(seat);
        return Response.ok(response);
    }

    //콘서트 자리 선택
    @PostMapping("/{concertId}/seats/choice")
    public Response<ConcertSeatInfoResponse>choiceSeat(@PathVariable("concertId")Long concertId, ChoiceSeatRequest choiceSeatRequest){
        Seat seat = reserveUseCase.choiceSeatAndReserve(concertId, choiceSeatRequest);
        ConcertSeatInfoResponse response = ConcertSeatInfoResponse.from(seat);
        return Response.ok(response);
    }

}
