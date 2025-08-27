package kr.hhplus.be.server.reservation.repository.entity;


import jakarta.persistence.*;
import kr.hhplus.be.server.user.repository.entity.UserEntity;
import kr.hhplus.be.server.concert.repository.entity.ConcertEntity;
import kr.hhplus.be.server.seat.domain.ReservationStatus;
import kr.hhplus.be.server.seat.repository.entity.SeatEntity;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "concert_id", nullable = false)
    private ConcertEntity concert;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "seat_id", nullable = false)
    private SeatEntity seat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Column(name = "reserved_at", nullable = false)
    private LocalDateTime reservedAt;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }

}