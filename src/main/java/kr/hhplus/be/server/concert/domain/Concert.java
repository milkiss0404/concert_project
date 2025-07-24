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
    private ConcertStatus concertStatus;
    private String desc;

    public String getConcertDate() {
        return concertSchedule.getConcertDate();
    }
    public String getConcertTime() {
        return concertSchedule.getConcertTime();
    }
    public void changeStatus(ConcertStatus concertStatus) {
        this.concertStatus = concertStatus;
    }
}
