package kr.hhplus.be.server.seat.application.service;

import kr.hhplus.be.server.concert.application.dtos.ConcertSeatInfoRequest;
import kr.hhplus.be.server.seat.domain.Seat;
import kr.hhplus.be.server.seat.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("좌석 조회 단위테스트")
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private SeatService seatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void seatInfo_shouldReturnSeat_whenSeatExists() {
        // given
        Long concertId = 1L;
        Long seatId = 40L;
        ConcertSeatInfoRequest request = new ConcertSeatInfoRequest(seatId);

        Seat expectedSeat = Seat.builder()
                .id(seatId)
                .build();

        when(seatRepository.seatInfo(concertId, seatId)).thenReturn(expectedSeat);

        // when
        Seat actualSeat = seatService.seatInfo(concertId, request);

        // then
        assertNotNull(actualSeat);
        assertEquals(expectedSeat.getId(), actualSeat.getId());
        verify(seatRepository, times(1)).seatInfo(concertId, seatId);
    }
}