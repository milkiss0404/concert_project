package kr.hhplus.be.server.seat.repository.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.concert.repository.entity.ConcertEntity;
import kr.hhplus.be.server.seat.domain.ReservationStatus;
import kr.hhplus.be.server.seat.domain.Seat;
import kr.hhplus.be.server.seat.domain.Zone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seats")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "concert_id")
    private ConcertEntity concert;

    @Enumerated(EnumType.STRING)
    private Zone zone;

    private String seatRow;

    private int seatNumber;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus = ReservationStatus.AVAILABLE;

    public Seat toSeat() {
        return Seat.builder()
                .id(id)
                .zone(zone)
                .seatRow(seatRow)
                .seatNumber(seatNumber)
                .reservationStatus(reservationStatus)
                .build();
    }
}