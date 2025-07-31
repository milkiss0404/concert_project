package kr.hhplus.be.server.seat.application.service;

import kr.hhplus.be.server.concert.application.dtos.ConcertSeatInfoRequest;
import kr.hhplus.be.server.concert.repository.JpaConcertRepository;
import kr.hhplus.be.server.concert.repository.entity.ConcertEntity;
import kr.hhplus.be.server.config.CustomTestContainer;
import kr.hhplus.be.server.seat.domain.ReservationStatus;
import kr.hhplus.be.server.seat.domain.Seat;
import kr.hhplus.be.server.seat.domain.Zone;
import kr.hhplus.be.server.seat.repository.SeatRepository;
import kr.hhplus.be.server.seat.repository.entity.SeatEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SeatService 통합테스트")
class SeatServiceTest extends CustomTestContainer {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private SeatService seatService;
    @Autowired
    private JpaConcertRepository concertRepository;

    @DisplayName("좌석 조회 통합테스트")
    @Test
    void seatInfo_shouldReturnSeat_whenSeatExists() {
        // given
        ConcertEntity concert = ConcertEntity.builder()
                .concertTitle("concert")
                .build();
        ConcertEntity savedConcert = concertRepository.save(concert);
        SeatEntity expectedSeat = SeatEntity.builder()
                .concert(concert)
                .seatNumber(1)
                .zone(Zone.S)
                .reservationStatus(ReservationStatus.AVAILABLE)
                .build();

        // 저장 후 생성된 ID 사용
        SeatEntity savedSeat = seatRepository.save(expectedSeat);
        Long seatId = savedSeat.getId();


        // when
        Seat actualSeat = seatService.seatInfo(savedConcert.getId(), seatId);

        // then
        assertNotNull(actualSeat);
        assertEquals(savedSeat.getId(), actualSeat.getId());
    }
}