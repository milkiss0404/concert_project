    package kr.hhplus.be.server.concert.controller;

    import io.restassured.RestAssured;
    import org.junit.jupiter.api.BeforeEach;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.boot.test.web.server.LocalServerPort;
    import org.springframework.test.context.ActiveProfiles;

    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    public class ApiTest {

        @LocalServerPort
        private int port;

        @BeforeEach
        public void setUp() {
            RestAssured.port = port;
        }
    }