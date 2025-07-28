package kr.hhplus.be.server.user.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import kr.hhplus.be.server.concert.controller.ApiTest;
import kr.hhplus.be.server.reservation.repository.JpaReservationRepository;
import kr.hhplus.be.server.user.application.dto.ChargingPointRequest;
import kr.hhplus.be.server.user.domain.Point;
import kr.hhplus.be.server.user.repository.JpaUserRepository;
import kr.hhplus.be.server.user.repository.entity.UserEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@DisplayName("유저 포인트 E2E 테스트")
public class UserControllerTest extends ApiTest {

    @Autowired
    private JpaUserRepository jpaUserRepository;


    private UserEntity savedUser;


    @BeforeEach
    public void setUp() {
        super.setUp();
        jpaUserRepository.deleteAll();
        Long userId = 1L;
        Point point = new Point(userId, 5000);
        savedUser = jpaUserRepository.save(
                new UserEntity(null, "유저명", "1234", point)
        );
    }


    @AfterEach
    public void tearDown() {
        jpaUserRepository.deleteAll();
    }

    @Test
    @DisplayName("사용자 캐시 충전 성공")
    void 사용자_캐시충전_API_성공() {
        ChargingPointRequest request = new ChargingPointRequest(savedUser.getId(), 10000);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/user/chargingPoint")
                .then()
                .statusCode(200)
                .body("value.id", equalTo(savedUser.getId().intValue()))
                .body("value.username", equalTo("유저명"))
                .body("value.point.userId", equalTo(savedUser.getId().intValue()))
                .body("value.point.point", equalTo(15000));
    }
}