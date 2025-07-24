package kr.hhplus.be.server.concert.domain;

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
    private String desc;

    public String getConcertDate() {
        return concertSchedule.getConcertDate();
    }
    public String getConcertTime() {
        return concertSchedule.getConcertTime();
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
