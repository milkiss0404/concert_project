package kr.hhplus.be.server.concert.application.service;

import kr.hhplus.be.server.concert.application.dtos.ConcertFindRequest;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.domain.ConcertSchedule;
import kr.hhplus.be.server.concert.domain.ConcertStatus;
import kr.hhplus.be.server.concert.modelMapper.ConcertModelMapper;
import kr.hhplus.be.server.concert.repository.ConcertRepository;
import kr.hhplus.be.server.concert.repository.entity.ConcertEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("콘서트 서비스 테스트")
@Nested
class ConcertServiceTest {

    @InjectMocks
    private ConcertService concertService;

    @Mock
    private ConcertRepository concertRepository;
    @Mock
    private ConcertModelMapper concertModelMapper;

    @Test
    @DisplayName("콘서트 찾기")
    void findConcert_returnsConcert() {
        // given
        LocalDateTime concertTime = LocalDateTime.of(2025, 7, 24, 18, 30);
        LocalDate concertDate = concertTime.toLocalDate();


        ConcertFindRequest request = new ConcertFindRequest(1L);
        ConcertEntity dummyConcert = ConcertEntity.builder()
                .id(1L)
                .concertTitle("테스트 콘서트")
                .concertSchedule(new ConcertSchedule(concertTime, concertDate ))
                .artist("테스트 아티스트")
                .concertStatus(ConcertStatus.SCHEDULED)
                .description("설명")
                .build();

        Concert concertDomain = Concert.builder()
                .id(1L)
                .concertTitle("테스트 콘서트")
                .concertSchedule(new ConcertSchedule(concertTime, concertDate ))
                .artist("테스트 아티스트")
                .concertStatus(ConcertStatus.SCHEDULED)
                .description("설명")
                .build();
        when(concertRepository.findById(1L)).thenReturn((dummyConcert));
        when(concertModelMapper.toDomain(dummyConcert)).thenReturn(concertDomain);

        // when
        Concert result = concertService.findConcert(request.concertId());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getConcertTitle()).isEqualTo("테스트 콘서트");

        verify(concertRepository, times(1)).findById(1L);
    }
}