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


    public String getConcertDate() {
        if (concertSchedule == null) {
            return null;
        }
            return concertSchedule.getConcertDateFormat();
    }
    public String getConcertTime() {
        if (concertSchedule == null) {
            return null;
        }
        return concertSchedule.getConcertTimeString();
    }
    public void ongoing() {
        this.concertStatus = ConcertStatus.ONGOING;
    }
    public void completed() {
        this.concertStatus = ConcertStatus.COMPLETED;
    }
    public void cancelled() {
        this.concertStatus = ConcertStatus.CANCELLED;
    }
}
