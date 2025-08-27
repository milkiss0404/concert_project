
설계 보고서
1. 개요

MSA 환경에서, 콘서트 예약 서비스와 결제, 좌석 서비스 등을 도메인별로 분리하여 운영할 경우, 서비스 확장에 따라 트랜잭션 처리에 대한 한계가 발생한다.
본 설계에서는 Redis Pub/Sub 기반 비동기 이벤트 전달을 활용하여, 분산 서버 환경에서의 이벤트 전파 및 트랜잭션 후속 처리를 구현하고, 트랜잭션 처리의 한계를 보완하기 위한 방안을 정리하였다.

2.시스템 구성
   
2.1 서비스 구성
   도메인   주요 역할
   Reservation Service   예약 생성, 예약 DB 저장, 예약 이벤트 발행
   UserService Service   결제 처리, 결제 완료 이벤트 발행
   SeatService   좌석 확정 처리

   2.2 이벤트 전달 구조

Redis Pub/Sub 채널: "concert-events"

이벤트 구조 (JSON + type)

{
"type": "ReservationCreatedEvent",
"payload": {
"reservationId": 123,
"seatId": 55,
"userId": 77
}
}


각 서비스는 채널을 구독(subscribe)하고, 이벤트 type을 기준으로 후속 처리를 수행

3. 트랜잭션 처리 한계
   
3.1 분산 환경에서의 한계

단일 JVM 트랜잭션 한계

Spring의 @Transactional은 같은 JVM 내 DB 트랜잭션에 한정됨

다중 인스턴스/멀티 서버 환경에서는 다른 JVM의 DB 상태를 보장할 수 없음

서비스 간 원자적 처리 불가

예약 → 결제 → 좌석 확정은 서로 다른 서비스와 DB를 사용

하나의 글로벌 트랜잭션으로 묶는 것이 불가능

동기식 호출 시 장애 발생 시 롤백 어려움

CAP 이슈

분산 환경에서는 일관성(Consistency), 가용성(Availability), 파티션 허용(Partition tolerance) 중 트레이드오프 존재

트랜잭션 일관성 확보 시 가용성이 떨어질 수 있음

4. 대응 방안
   4.1 비동기 이벤트 기반 아키텍처

Redis Pub/Sub 또는 Kafka를 통해 서비스 간 이벤트 비동기 전달

트랜잭션 커밋 후 이벤트 발행 (@TransactionalEventListener(phase = AFTER_COMMIT))

각 서비스는 수신한 이벤트를 기반으로 독립적으로 DB 처리

4.2 Saga 패턴 적용

분산 트랜잭션 대신 로컬 트랜잭션 + 보상 트랜잭션 사용

예시:

예약 생성 → 예약 DB commit → ReservationCreatedEvent 발행

결제 서비스 → 결제 DB commit → PaymentCompletedEvent 발행

결제 실패 시 → 예약 취소 이벤트 발행 → 예약 서비스 롤백 처리

4.3 이벤트 타입 기반 분기

JSON 메시지에 type 필드 포함 → 단일 채널에서 다양한 이벤트 처리

이벤트 수신 서비스에서 switch(type)로 분기 처리

4.4 재시도/Dead Letter Queue

메시지 처리 실패 시 재시도 정책 적용

반복 실패 이벤트는 Dead Letter Queue로 이동 → 수동/자동 재처리

5. 장점
   확장성: 서비스별 독립적 확장 가능

   트랜잭션 관리: 글로벌 트랜잭션 불필요, 로컬 트랜잭션 + 이벤트로 유연한 처리 가능

   실시간성   Redis Pub/Sub 또는 Kafka를 통해 거의 실시간 이벤트 전파


6. 한계 및 고려사항

Redis Pub/Sub은 메시지 유실 가능성 존재 → 중요 이벤트는 Kafka 또는 Redis Stream 고려

이벤트 순서 보장 필요 시 Stream 기반 구현 필요

트랜잭션 보상 처리 로직 설계 필요

장애 상황에서 서비스 간 상태 불일치 가능 → Saga 패턴 및 모니터링 필요

7. 결론

분산 서버 환경에서 도메인별 DB와 서비스 분리를 하더라도, Redis Pub/Sub 기반 이벤트 전달과 Saga 패턴을 활용하면 글로벌 트랜잭션 없이도 안정적인 이벤트 흐름과 후속 트랜잭션 처리가 가능함

메시지 기반 비동기 처리 구조를 설계하면, 서비스 확장성과 내결함성을 확보하면서 트랜잭션 한계를 극복할 수 있음