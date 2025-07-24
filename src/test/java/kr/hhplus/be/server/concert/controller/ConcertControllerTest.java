package kr.hhplus.be.server.concert.controller;

import io.restassured.RestAssured;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.repository.ConcertRepository;
import kr.hhplus.be.server.concert.repository.JpaConcertRepository;
import kr.hhplus.be.server.concert.repository.entity.ConcertEntity;
import kr.hhplus.be.server.config.ui.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("콘서트 조회 E2E 테스트")
public class ConcertControllerTest extends ApiTest {

    @Autowired
    private JpaConcertRepository jpaConcertRepository;

    private Long savedConcertId;

    @BeforeEach
    public void setUp() {
        super.setUp();
        jpaConcertRepository.deleteAll();
        ConcertEntity 테스트_콘서트 = ConcertEntity.builder()
                .concertTitle("테스트 콘서트")
                .build();
        ConcertEntity saved = jpaConcertRepository.save(테스트_콘서트);
        savedConcertId = saved.getId();  // 실제 할당된 ID 저장
    }

    @Test
    void findConcert_returnsOkAndConcertData() {
        RestAssured
                .given()
                .queryParam("concertId", savedConcertId)  // 동적으로 넣기
                .when()
                .get("/concert/find")
                .then()
                .statusCode(200)
                .body("code", equalTo(0))
                .body("message", equalTo("ok"))
                .body("value.id", equalTo(savedConcertId.intValue()))
                .body("value.concertTitle", notNullValue());
    }
}