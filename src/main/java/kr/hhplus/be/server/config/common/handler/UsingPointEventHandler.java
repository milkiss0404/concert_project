package kr.hhplus.be.server.config.common.handler;

import kr.hhplus.be.server.reservation.application.dto.CancelReservationEvent;
import kr.hhplus.be.server.user.application.dto.UsingPointEvent;
import kr.hhplus.be.server.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UsingPointEventHandler {
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @EventListener
    @Transactional
    //핵심 비즈니스는 동기로 처리해야 ACID 를 보장할수있기때문에 동기식으로 처리
    public void usingPointEvent(UsingPointEvent event){
          try {
              userService.usingUserPoint(event.user(),event.price());
          }catch (Exception e){
              eventPublisher.publishEvent(new CancelReservationEvent(event.reservation()));
          }
    }
}
