package kr.hhplus.be.server.queue.controller;

import kr.hhplus.be.server.concert.application.service.ConcertService;
import kr.hhplus.be.server.concert.application.userCase.ReserveUseCase;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.domain.ConcertSchedule;
import kr.hhplus.be.server.concert.domain.ConcertStatus;
import kr.hhplus.be.server.concert.repository.JpaConcertRepository;
import kr.hhplus.be.server.concert.repository.entity.ConcertEntity;
import kr.hhplus.be.server.config.CustomTestContainer;
import kr.hhplus.be.server.config.ui.Response;
import kr.hhplus.be.server.queue.jwt.JwtTokenProvider;
import kr.hhplus.be.server.queue.repository.RedisZSetRepository;
import kr.hhplus.be.server.seat.repository.JpaSeatRepository;
import kr.hhplus.be.server.seat.repository.entity.SeatEntity;
import kr.hhplus.be.server.user.repository.JpaUserRepository;
import kr.hhplus.be.server.user.repository.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ZsetController 테스트")
class ZSetQueueControllerTest extends CustomTestContainer {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ZSetQueueController zSetQueueController;

    @Autowired
    private RedisZSetRepository redisZSetRepository;

    @Autowired
    private ReserveUseCase reserveUseCase;

    @Autowired
    private JpaConcertRepository jpaConcertRepository;

    @Autowired
    private JpaSeatRepository jpaSeatRepository;

    @Autowired
    private JpaUserRepository jpauserRepository;
    private Long concertId1, concertId2, concertId3;
    private Long seatId1, seatId2, seatId3, seatId4, seatId5, seatId6;
    private Long userId1, userId2, userId3, userId4, userId5, userId6;

    @BeforeEach
    void setUp() {
        redisTemplate.delete("concertRanking");

        // Redis 초기값
        redisTemplate.opsForZSet().add("concertRanking", "1", 10);
        redisTemplate.opsForZSet().add("concertRanking", "2", 20);
        redisTemplate.opsForZSet().add("concertRanking", "3", 30);
        redisTemplate.opsForZSet().add("concertRanking", "4", 40);
        redisTemplate.opsForZSet().add("concertRanking", "5", 50);

        LocalDateTime concertTime = LocalDateTime.of(2025, 7, 24, 18, 30);
        LocalDate concertDate = concertTime.toLocalDate();
        ConcertSchedule schedule = new ConcertSchedule(concertTime, concertDate);

        // 콘서트 엔티티 DB 초기화 및 실제 ID 저장
        ConcertEntity concert1 = jpaConcertRepository.save(new ConcertEntity(null, "레드벨벳 콘서트", schedule, "레드벨벳", ConcertStatus.SCHEDULED, "ㅇㅁㄴㅇㅁㄴ"));
        ConcertEntity concert2 = jpaConcertRepository.save(new ConcertEntity(null, "블랙핑크 콘서트", schedule, "블랙핑크", ConcertStatus.SCHEDULED, "ㅇㄴㅁㅇㄴㅁ"));
        ConcertEntity concert3 = jpaConcertRepository.save(new ConcertEntity(null, "아이브 콘서트", schedule, "아이브", ConcertStatus.SCHEDULED, "ㅇㅁㄴㅇㄴㅁㅇ"));

        concertId1 = concert1.getId();
        concertId2 = concert2.getId();
        concertId3 = concert3.getId();

        // 유저 엔티티 DB 초기화 및 실제 ID 저장
        UserEntity user1 = jpauserRepository.save(new UserEntity(null, "user10"));
        UserEntity user2 = jpauserRepository.save(new UserEntity(null, "user11"));
        UserEntity user3 = jpauserRepository.save(new UserEntity(null, "user12"));
        UserEntity user4 = jpauserRepository.save(new UserEntity(null, "user13"));
        UserEntity user5 = jpauserRepository.save(new UserEntity(null, "user14"));
        UserEntity user6 = jpauserRepository.save(new UserEntity(null, "user15"));

        userId1 = user1.getId();
        userId2 = user2.getId();
        userId3 = user3.getId();
        userId4 = user4.getId();
        userId5 = user5.getId();
        userId6 = user6.getId();

        // 좌석 엔티티 DB 초기화 및 실제 ID 저장
        SeatEntity seat1 = jpaSeatRepository.save(new SeatEntity(null, "A1"));
        SeatEntity seat2 = jpaSeatRepository.save(new SeatEntity(null, "A2"));
        SeatEntity seat3 = jpaSeatRepository.save(new SeatEntity(null, "A3"));
        SeatEntity seat4 = jpaSeatRepository.save(new SeatEntity(null, "B1"));
        SeatEntity seat5 = jpaSeatRepository.save(new SeatEntity(null, "B2"));
        SeatEntity seat6 = jpaSeatRepository.save(new SeatEntity(null, "B3"));

        seatId1 = seat1.getId();
        seatId2 = seat2.getId();
        seatId3 = seat3.getId();
        seatId4 = seat4.getId();
        seatId5 = seat5.getId();
        seatId6 = seat6.getId();
    }

    @DisplayName("콘서트 시작 -> 유저큐입장 -> 퇴장 -> 콘서트종료 테스트 ")
    @Test
    void testEnterQueueAndTokenFlow() {
        Long concertId = 1L;
        Long userId = 100L;

        // 1️⃣ 콘서트 시작
        ResponseEntity<String> startResponse = restTemplate.postForEntity(
                "/" + concertId + "/start", null, String.class
        );
        assertEquals(HttpStatus.OK, startResponse.getStatusCode());

        // 2️⃣ 유저 큐 입장
        ResponseEntity<String> tokenResponse = restTemplate.postForEntity(
                "/" + concertId + "/queue/zset?userId=" + userId,
                null,
                String.class
        );
        assertEquals(HttpStatus.OK, tokenResponse.getStatusCode());

        String token = tokenResponse.getBody();
        assertNotNull(token, "JWT 토큰이 생성되어야 합니다");

        // 3️⃣ Redis에 토큰이 저장되었는지 확인
        String storedUserId = redisTemplate.opsForValue().get("queue:token:" + token);
        assertEquals(String.valueOf(userId), storedUserId);

        // 4️⃣ 콘서트 종료
        ResponseEntity<String> endResponse = restTemplate.postForEntity(
                "/" + concertId + "/end", null, String.class
        );
        assertEquals(HttpStatus.OK, endResponse.getStatusCode());
    }

    @DisplayName("incr 리셋")
    @Test
    void testCounterReset() {
        Long concertId = 1L;

        // Counter 초기값 세팅
        redisTemplate.opsForValue().set("concertQueueCounter:" + concertId, "10");

        // 리셋 호출
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/" + concertId + "/queue/zset/counterReset", null, String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // 값 확인
        String counter = redisTemplate.opsForValue().get("concertQueueCounter:" + concertId);
        assertEquals("0", counter, "카운터가 0으로 리셋되어야 합니다");
    }

    @DisplayName("탑 5 콘서트 랭킹 조회")
    @Test
    void testGetTopConcertsMoreThanAvailable() {
        // top 5 조회
        Response<List<String>> response = zSetQueueController.concertRanking(5);
        List<String> topConcerts = response.value();

        assertNotNull(topConcerts);
        assertEquals(5, topConcerts.size());
        assertThat(Integer.parseInt(topConcerts.get(0))).isEqualTo(5);
        assertThat(Integer.parseInt(topConcerts.get(1))).isEqualTo(4);
        assertThat(Integer.parseInt(topConcerts.get(2))).isEqualTo(3);
    }

    @DisplayName("탑 3 콘서트 조회 실제 예약시")
    @Test
    void testReservationWithMultipleParameters(Long concertId, Long seatId, Long userId) {
        // fresh 엔티티 조회
        reserveUseCase.choiceSeatAndReserve(concertId1, seatId1, userId1);
        reserveUseCase.choiceSeatAndReserve(concertId1, seatId2, userId2);
        reserveUseCase.choiceSeatAndReserve(concertId1, seatId3, userId3);
        reserveUseCase.choiceSeatAndReserve(concertId2, seatId4, userId4);
        reserveUseCase.choiceSeatAndReserve(concertId2, seatId5, userId5);
        reserveUseCase.choiceSeatAndReserve(concertId3, seatId6, userId6);

        // Redis top 3 확인
        List<String> topConcerts = redisZSetRepository.getTopConcerts(3);
        assertThat(topConcerts).isNotNull();
        assertThat(Integer.parseInt(topConcerts.get(0))).isEqualTo(1);
        assertThat(Integer.parseInt(topConcerts.get(1))).isEqualTo(2);
        assertThat(Integer.parseInt(topConcerts.get(2))).isEqualTo(3);
    }
}