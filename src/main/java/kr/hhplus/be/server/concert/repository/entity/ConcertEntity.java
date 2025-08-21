package kr.hhplus.be.server.concert.repository.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.domain.ConcertSchedule;
import kr.hhplus.be.server.concert.domain.ConcertStatus;
import lombok.*;

@Entity
@Table(name = "concerts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ConcertEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String concertTitle;
    @Embedded
    private ConcertSchedule concertSchedule;
    private String artist;
    @Enumerated(EnumType.STRING)
    private ConcertStatus concertStatus = ConcertStatus.SCHEDULED;
    @Lob
    private String description;

}