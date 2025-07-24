package kr.hhplus.be.server.concert.controller;

import kr.hhplus.be.server.concert.application.dtos.ConcertFindRequest;
import kr.hhplus.be.server.concert.application.dtos.ConcertFindResponse;
import kr.hhplus.be.server.concert.application.service.ConcertService;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.repository.entity.ConcertEntity;
import kr.hhplus.be.server.config.ui.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/concert")
@RestController
@RequiredArgsConstructor
public class ConcertController {
    private final ConcertService concertService;

    @GetMapping("/find")
    public Response<ConcertFindResponse> findConcert(ConcertFindRequest concertFindRequest) {
        Concert concert = concertService.findConcert(concertFindRequest);
        ConcertFindResponse response = ConcertFindResponse.from(concert);
        return Response.ok(response);
    }
}
