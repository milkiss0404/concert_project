package kr.hhplus.be.server.concert.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import kr.hhplus.be.server.concert.application.dtos.ConcertFindRequest;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.repository.ConcertRepository;
import kr.hhplus.be.server.concert.repository.JpaConcertRepository;
import kr.hhplus.be.server.concert.repository.entity.ConcertEntity;
import kr.hhplus.be.server.config.ui.Response;
import kr.hhplus.be.server.reservation.repository.JpaReservationRepository;
import kr.hhplus.be.server.seat.domain.ReservationStatus;
import kr.hhplus.be.server.seat.domain.Zone;
import kr.hhplus.be.server.seat.repository.JpaSeatRepository;
import kr.hhplus.be.server.seat.repository.entity.SeatEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("콘서트 조회 E2E 테스트")
@Nested
 public class ConcertControllerTest extends ApiTest {

    @Autowired
    private JpaConcertRepository jpaConcertRepository;
    @Autowired
    private JpaSeatRepository jpaSeatRepository;

    private Long testConcertId;
    private Long testSeatId;

    @BeforeEach
    public void setUp() {
        super.setUp();
        jpaSeatRepository.deleteAll();
        jpaConcertRepository.deleteAll();

        ConcertEntity 테스트_콘서트 = ConcertEntity.builder()
                .concertTitle("테스트 콘서트")
                .build();
        ConcertEntity saved = jpaConcertRepository.save(테스트_콘서트);
        testConcertId = saved.getId();

        SeatEntity 테스트_좌석= SeatEntity.builder()
                .concert(saved)
                .seatRow("1")
                .zone(Zone.VIP)
                .seatNumber(10)
                .reservationStatus(ReservationStatus.AVAILABLE)
                .build();
        SeatEntity save = jpaSeatRepository.save(테스트_좌석);
        testSeatId = save.getId();
    }
    @DisplayName("콘서트 조회")
    @Test
    void findConcert_returnsOkAndConcertData() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new ConcertFindRequest(testConcertId))
                .when()
                .get("/concert/find")
                .then()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("message", equalTo("ok"))
                .body("value.id", equalTo(testConcertId.intValue()))
                .body("value.concertTitle", notNullValue());
    }
    @DisplayName("콘서트 좌석조회")
    @Test
    void findSeat_returnsOkAndSeatData() {
        RestAssured
                .given()
                .queryParam("seatId", testSeatId)
                .when()
                .get("/concert/" + testConcertId + "/seats")
                .then()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("message", equalTo("ok"))
                .body("value.id", equalTo(testSeatId.intValue()))
                .body("value.reservationStatus", equalTo("AVAILABLE"));
    }
}