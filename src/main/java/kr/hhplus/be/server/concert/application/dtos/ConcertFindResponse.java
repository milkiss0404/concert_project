package kr.hhplus.be.server.concert.application.dtos;

import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.domain.ConcertSchedule;
import kr.hhplus.be.server.concert.domain.ConcertStatus;

public record ConcertFindResponse(Long id,
                                  String concertTitle,
                                  ConcertSchedule concertSchedule,
                                  String artist,
                                  ConcertStatus concertStatus,
                                  String description) {
    public static ConcertFindResponse from(Concert concert) {
        return new ConcertFindResponse(concert.getId(),
                concert.getConcertTitle(),
                concert.getConcertSchedule(),
                concert.getArtist(),
                concert.getConcertStatus(),
                concert.getDescription()
        );
    }
}
