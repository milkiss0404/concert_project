package kr.hhplus.be.server.concert.domain;

import kr.hhplus.be.server.concert.repository.entity.ConcertEntity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Concert {
    private Long id;
    private String concertTitle;
    private ConcertSchedule concertSchedule;
    private String artist;
    private ConcertStatus concertStatus = ConcertStatus.SCHEDULED;
    private String description;


    public static ConcertEntity toConcertEntity(Concert concert) {
        return ConcertEntity.builder()
                .id(concert.id)
                .concertTitle(concert.concertTitle)
                .concertSchedule(concert.concertSchedule)
                .artist(concert.artist)
                .concertStatus(concert.concertStatus)
                .description(concert.description)
                .build();
    }

    public String getConcertDate() {
        return concertSchedule.getConcertDateFormat();
    }
    public String getConcertTime() {
        return concertSchedule.getConcertTimeString();
    }
    public void changeStatus_ONGOING() {
        this.concertStatus = ConcertStatus.ONGOING;
    }
    public void changeStatus_COMPLETED() {
        this.concertStatus = ConcertStatus.COMPLETED;
    }
    public void changeStatus_CANCELLED() {
        this.concertStatus = ConcertStatus.CANCELLED;
    }
}
