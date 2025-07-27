package kr.hhplus.be.server.reservation.modelmapper;

import kr.hhplus.be.server.reservation.domain.Reservation;
import kr.hhplus.be.server.reservation.repository.entity.ReservationEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationModelMapper {
    private final ModelMapper modelMapper;

    public ReservationEntity toEntity(Reservation domain) {
        return modelMapper.map(domain, ReservationEntity.class);
    }
    public Reservation toDomain(ReservationEntity Entity) {
        return modelMapper.map(Entity, Reservation.class);
    }
}
