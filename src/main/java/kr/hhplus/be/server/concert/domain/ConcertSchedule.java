package kr.hhplus.be.server.concert.domain;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ConcertSchedule {
    public ConcertSchedule(LocalDateTime concertTime, LocalDate concertDate) {
        this.concertTime = concertTime;
        this.concertDate = concertDate;
    }

    private LocalDate concertDate;
    private LocalDateTime concertTime;


    public String getConcertDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return concertDate.format(formatter);
    }


    public String getConcertTime() {
        return concertTime.getHour() + "시" + concertTime.getMinute() + "분";
    }
}
