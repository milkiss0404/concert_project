package kr.hhplus.be.server.seat.modelMapper;

import kr.hhplus.be.server.seat.domain.Seat;
import kr.hhplus.be.server.seat.repository.entity.SeatEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SeatModelMapper {
    private final ModelMapper modelMapper;

    public SeatEntity toEntity(Seat domain) {
        return modelMapper.map(domain, SeatEntity.class);
    }
    public Seat toDomain(SeatEntity entity) {
        return modelMapper.map(entity, Seat.class);
    }

}
