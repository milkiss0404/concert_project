package kr.hhplus.be.server.concert.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Embeddable
public class ConcertSchedule {
    public ConcertSchedule(LocalDateTime concertTime, LocalDate concertDate) {
        this.concertTime = concertTime;
        this.concertDate = concertDate;
    }

    public ConcertSchedule() {
    }

    private LocalDate concertDate;
    private LocalDateTime concertTime;


    public String getConcertDateFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return concertDate.format(formatter);
    }


    public String getConcertTimeString() {
        return concertTime.getHour() + "시" + concertTime.getMinute() + "분";
    }
}
