Redis ZSet 기반 콘서트 대기열 관리 시스템 보고서
1. 시스템 개요

본 시스템은 인기 콘서트 예약 시 동시 접속자 증가로 인한 DB 부하와 데이터 충돌 문제를 해결하기 위해 **Redis ZSet(정렬된 집합, Sorted Set)**을 활용한 대기열 관리 구조를 구현하였다.
주요 목표는 다음과 같다:

유저 대기열 순서 관리

예약 처리 속도 향상

콘서트 인기 순위 실시간 반영

트랜잭션 과부하 최소화

2. Redis ZSet 구조
   항목	설명
   Key	concertQueue:{concertId}
   Value	userId
   Score	incr 값으로 대기열 순번 또는 예약 반영 점수

Key: 콘서트 ID별로 ZSet을 구분하여 관리

Value: 해당 콘서트에 입장한 유저 ID

Score: ZSet 내 순서 관리 및 인기 순위 반영에 사용

3. 기능 흐름
   3.1 콘서트 시작

콘서트 상태 변경

DB와 Redis 모두에서 콘서트 상태를 "대기중" 또는 "예약 가능" 상태로 변경

Redis 초기화

해당 콘서트의 ZSet 생성: concertQueue:{concertId}

초기 점수 세팅 (예: 순번 0부터 시작)

필요 시 카운터 초기화: 예약 점수 및 순번 관리용

3.2 유저 큐 입장

API 호출

POST /{concertId}/queue/zset?userId={userId}

ZSet 등록

Redis ZSet에 유저 ID 추가:

ZADD concertQueue:{concertId} {currentScore} {userId}


Score는 현재 대기열 순번 기준 incr 값 사용

JWT 토큰 발급

유저 식별 및 예약 인증을 위해 JWT 발급

Token Redis 저장

queue:token:{token}에 {userId, concertId} 등 매핑 저장

3.3 예약 처리

좌석 선택

ReserveUseCase에서 좌석 선택 후 예약 처리

SeatEntity.getConcert()를 통해 콘서트 정보 조회

ZSet Score 업데이트

예약 성공 시 ZSet score 증가 → 인기 순위 반영

예시: ZINCRBY concertQueue:{concertId} 1 {userId}

3.4 콘서트 종료

콘서트 상태 변경

DB에서 상태를 "종료" 또는 "완료"로 변경

Redis 초기화

ZSet 삭제: DEL concertQueue:{concertId}

카운터 초기화 (incr 값 초기화)

4. 설계 장점

빠른 대기열 처리: Redis ZSet 기반으로 O(log N) 순위 관리 가능

동시성 문제 최소화: DB 트랜잭션 부하 없이 실시간 순서 반영

인기 콘서트 순위 실시간 반영: 예약 점수 증가 → 실시간 인기 지표 제공

토큰 기반 인증: JWT + Redis 매핑으로 인증 및 예약 안전성 확보