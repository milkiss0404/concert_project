package kr.hhplus.be.server.seat.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import kr.hhplus.be.server.concert.controller.ApiTest;
import kr.hhplus.be.server.concert.domain.ConcertSchedule;
import kr.hhplus.be.server.concert.domain.ConcertStatus;
import kr.hhplus.be.server.concert.repository.JpaConcertRepository;
import kr.hhplus.be.server.concert.repository.entity.ConcertEntity;
import kr.hhplus.be.server.seat.domain.Seat;
import kr.hhplus.be.server.seat.domain.Zone;
import kr.hhplus.be.server.seat.domain.ReservationStatus;
import kr.hhplus.be.server.seat.repository.JpaSeatRepository;
import kr.hhplus.be.server.seat.repository.SeatRepository;
import kr.hhplus.be.server.seat.repository.entity.SeatEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

@DisplayName("Seat 컨트롤러 통합테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SeatControllerTest extends ApiTest {

    @Autowired
    JpaSeatRepository jpaSeatRepository;


    @Autowired
    JpaConcertRepository jpaConcertRepository;

    private Long testConcertId;
    @BeforeEach
    void setup() {
        ConcertEntity 테스트_콘서트 = ConcertEntity.builder()
                .concertTitle("테스트 콘서트")
                .build();
        ConcertEntity saved = jpaConcertRepository.save(테스트_콘서트);
        testConcertId = saved.getId();

        SeatEntity seatEntityBuilder = SeatEntity.builder().concert(saved).zone(Zone.VIP).reservationStatus(ReservationStatus.AVAILABLE).build();
        jpaSeatRepository.save(seatEntityBuilder);
    }

    @Test
    @DisplayName("한 콘서트의 예약현황 별, 좌석 등급별 좌석조회")
    void testFindSeatWhereConcertAndStatus() {
        Long concertId = testConcertId;
        String zone = Zone.VIP.name();
        String status = ReservationStatus.AVAILABLE.name();

        given()
                .accept(ContentType.JSON)
                .queryParam("concertId", concertId)
                .queryParam("zone", zone)
                .queryParam("status", status)
                .when()
                .get("/seat/concert/status")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("$", not(empty()))
                .body("value.zone", everyItem(equalTo(zone)))
                .body("value.reservationStatus", everyItem(equalTo(status)))
                .body("value.concert.id", everyItem(equalTo(concertId.intValue())));
    }
}
