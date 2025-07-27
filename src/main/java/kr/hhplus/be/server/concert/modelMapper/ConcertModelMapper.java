package kr.hhplus.be.server.concert.modelMapper;

import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.repository.entity.ConcertEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ConcertModelMapper {
    private final ModelMapper modelMapper;

    public ConcertEntity toEntity(Concert domain) {
        return modelMapper.map(domain, ConcertEntity.class);
    }
    public Concert toDomain(ConcertEntity entity) {
        return modelMapper.map(entity, Concert.class);
    }
}
