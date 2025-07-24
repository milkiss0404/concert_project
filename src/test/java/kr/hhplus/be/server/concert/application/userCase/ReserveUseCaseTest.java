package kr.hhplus.be.server.concert.application.userCase;

import kr.hhplus.be.server.concert.domain.ConcertSchedule;
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
import kr.hhplus.be.server.seat.repository.SeatRepository;
import kr.hhplus.be.server.seat.repository.entity.SeatEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ReserveUseCaseTest {

    @Autowired
    private ReserveUseCase reserveUseCase;
    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private JpaConcertRepository jpaConcertRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private JpaReservationRepository jpaReservationRepository;

    @Autowired
    private JpaSeatRepository jpaSeatRepository;

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
        ConcertEntity concertEntity = Concert.toConcertEntity(concert);
        ConcertEntity saveEntity = jpaConcertRepository.save(concertEntity);

        Seat seat = Seat.builder()
                .zone(Zone.VIP)
                .seatNumber(10)
                .reservationStatus(ReservationStatus.AVAILABLE)
                .seatRow("2열")
                .build();
        SeatEntity seatEntity = Seat.toSeatEntity(seat);
        SeatEntity saveSeatEntity = jpaSeatRepository.save(seatEntity);
        concertId = saveEntity.getId();
        seatId = saveSeatEntity.getId();

        userIds = new ArrayList<>();
        for (Long i = 0L; i < 100L; i++) {
            UserEntity user = jpaUserRepository.save(new UserEntity());
            userIds.add(user.getId());
        }
    }

    @Test
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
                    barrier.await(); // 모든 스레드 동시에 출발
                    reserveUseCase.choiceSeat(concertId, new ChoiceSeatRequest(userIds.get(userIndex), seatId));
                } catch (Throwable t) {
                    exceptions.add(t);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        List<ReservationEntity> reservations = jpaReservationRepository.findAll();
        assertThat(reservations).hasSize(1);
        assertThat(exceptions.size()).isEqualTo(99);
    }
}
