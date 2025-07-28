package kr.hhplus.be.server.concert.application.userCase;

import kr.hhplus.be.server.concert.domain.ConcertSchedule;
import kr.hhplus.be.server.concert.modelMapper.ConcertModelMapper;
import kr.hhplus.be.server.user.domain.Point;
import kr.hhplus.be.server.user.repository.JpaUserRepository;
import kr.hhplus.be.server.user.repository.entity.UserEntity;
import kr.hhplus.be.server.concert.application.dtos.ChoiceSeatRequest;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.repository.JpaConcertRepository;
import kr.hhplus.be.server.concert.repository.entity.ConcertEntity;
import kr.hhplus.be.server.reservation.repository.JpaReservationRepository;
import kr.hhplus.be.server.reservation.repository.entity.ReservationEntity;
import kr.hhplus.be.server.seat.domain.ReservationStatus;
import kr.hhplus.be.server.seat.domain.Seat;
import kr.hhplus.be.server.seat.domain.Zone;
import kr.hhplus.be.server.seat.repository.JpaSeatRepository;
import kr.hhplus.be.server.seat.repository.entity.SeatEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@DisplayName("예약 E2E 테스트")
@Nested
class ReserveUseCaseTest {

    @Autowired
    private ReserveUseCase  reserveUseCase;

    @Autowired
    private JpaConcertRepository jpaConcertRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private JpaReservationRepository jpaReservationRepository;

    @Autowired
    private JpaSeatRepository jpaSeatRepository;

    @Autowired
    private ConcertModelMapper concertModelMapper;

    private Long concertId;
    private Long seatId;
    private List<Long> userIds;
    @BeforeEach
    void setUp() {
        LocalDateTime concertTime = LocalDateTime.of(2025, 7, 24, 18, 30);
        LocalDate concertDate = concertTime.toLocalDate();

        Concert concert = Concert.builder()
                .concertTitle("테스트콘서트")
                .concertSchedule(new ConcertSchedule(concertTime, concertDate))
                .build();

        ConcertEntity savedConcertEntity = jpaConcertRepository.save(concertModelMapper.toEntity(concert));

        Seat seat = Seat.builder()
                .concert(concert)
                .zone(Zone.VIP)
                .seatNumber(10)
                .reservationStatus(ReservationStatus.AVAILABLE)
                .seatRow("2열")
                .build();

        SeatEntity seatEntity = SeatEntity.builder()
                .concert(savedConcertEntity)
                .zone(seat.getZone())
                .seatNumber(seat.getSeatNumber())
                .seatRow(seat.getSeatRow())
                .reservationStatus(seat.getReservationStatus())
                .build();


        SeatEntity savedSeatEntity = jpaSeatRepository.save(seatEntity);

        concertId = savedConcertEntity.getId();
        seatId = savedSeatEntity.getId();

        userIds = new ArrayList<>();
        for (Long i = 1L; i < 100L; i++) {
            UserEntity user = jpaUserRepository.save(new UserEntity(null,"유저명","1234",new Point(i, 1000000)));
            userIds.add(user.getId());
        }
    }
    @Test
    @DisplayName("동시에_100명_좌석예약_테스트")
    void 동시에_100명_좌석예약_테스트() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        CyclicBarrier barrier = new CyclicBarrier(threadCount); // 동시 출발

        List<Throwable> exceptions = new CopyOnWriteArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            final int userIndex = i;
            executor.submit(() -> {
                try {
                    barrier.await();
                    var seat = reserveUseCase.choiceSeatAndReserve(concertId, new ChoiceSeatRequest(userIds.get(userIndex), seatId));
                    System.out.println("예약 성공: " + seat.getId() + " by user " + userIds.get(userIndex));
                } catch (Throwable t) {
                    exceptions.add(t);
                    t.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        List<ReservationEntity> reservations = jpaReservationRepository.findAll();
        System.out.println("총 예약 건수: " + reservations.size());
        assertThat(reservations).hasSize(1);
        assertThat(exceptions.size()).isEqualTo(99);
    }

    @Test
    @DisplayName("좌석예약_테스트_금액사용")
    void usingCash() {
        // given
        Long userId = 1L;
        UserEntity user = new UserEntity(userId,"유저명","1234",new Point(userId, 1000000));
        jpaUserRepository.save(user);
        // when
        Seat seat = reserveUseCase.choiceSeatAndReserve(concertId, new ChoiceSeatRequest(user.getId(), seatId));
        // then
        Optional<UserEntity> byId = jpaUserRepository.findById(userId);

        assertThat(byId.get().getPoint()).isEqualTo(850000);
        assertThat(seat.getReservationStatus()).isEqualTo(ReservationStatus.RESERVED);
    }

}