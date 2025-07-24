package kr.hhplus.be.server.concert.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("콘서트 테스트")
class ConcertTest {
    Concert concert;

    @BeforeEach
    void setup() {
        LocalDateTime concertTime = LocalDateTime.of(2025, 7, 24, 18, 30);
        LocalDate concertDate = concertTime.toLocalDate();

        ConcertSchedule concertSchedule =
                new ConcertSchedule(concertTime,concertDate);

        concert = Concert.builder()
                .id(1L)
                .concertTitle("레드벨벳 콘서트!@")
                .concertSchedule(concertSchedule)
                .artist("레드벨벳")
                .concertStatus(ConcertStatus.SCHEDULED)
                .description("비고")
                .build();
    }

    @Test
    @DisplayName("콘서트 시간 조회 테스트")
    void selectConcertTime() {
        // given
        // when
        String concertTime = concert.getConcertTime();
        // then
        assertEquals("18시30분",concertTime);
    }


    @Test
    @DisplayName("콘서트 날짜 조회 테스트")
    void selectConcertDate() {
        // given
        // when
        String concertDate = concert.getConcertDate();
        // then
        assertEquals("2025-07-24",concertDate);
    }

    @Test
    @DisplayName("콘서트 상태 => 취소")
    void concertChangeStatusCANCELLED() {
        // given
        // when
         concert.changeStatus_CANCELLED();
        // then
        assertEquals(concert.getConcertStatus(),ConcertStatus.CANCELLED);
    }
    @Test
    @DisplayName("콘서트 상태 => 시작")
    void concertChangeStatusONGOING() {
        // given
        // when
         concert.changeStatus_ONGOING();
        // then
        assertEquals(concert.getConcertStatus(),ConcertStatus.ONGOING);
    }
    @Test
    @DisplayName("콘서트 상태 => 종료")
    void concertChangeStatusCOMPLETED() {
        // given
        // when
        concert.changeStatus_COMPLETED();
        // then
        assertEquals(concert.getConcertStatus(),ConcertStatus.COMPLETED);
    }



}