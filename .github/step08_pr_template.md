## :pushpin: PR 제목 규칙
[STEP08] 김우철 - concert

---
### **핵심 체크리스트** :white_check_mark:

#### :one: Infrastructure Layer (3개)
- [ ] 기존 설계된 테이블 구조에 대한 개선점이 반영되었는가? (선택)
- [ ] Repository 및 데이터 접근 계층이 역할에 맞게 분리되어 있는가?
- [ ] MySQL 기반으로 연동되고 동작하는가?

#### :two: 통합 테스트 (2개)
- [ ] infrastructure 레이어를 포함하는 통합 테스트가 작성되었는가?
- [ ] 핵심 기능에 대한 흐름이 테스트에서 검증되었는가?

#### :three: DB 성능 최적화 (3개)
- [ ] 조회 성능 저하 가능성이 있는 기능을 식별하였는가?
- [ ] 쿼리 실행계획(Explain) 기반으로 문제를 분석하였는가?
- [ ] 인덱스 설계 또는 쿼리 구조 개선 등 해결방안을 도출하였는가?

---


콘서트의 좌석등급별 예약 상태 API 를 구성해보며 자주 조회할수있고, 추후 동시성 처리도 해야할것으로보이는 쿼리를
//라고 생각했고 쿼리플랜을 검색해봤습니다.
현재 EXPLAIN SELECT * FROM seats WHERE zone = 'vip' AND concert_id = 9 AND reservation_status = 'AVAILABLE'; 쿼리의 실행 계획을 살펴보면, type이 ref로 표시되어 있고 사용된 인덱스가 FK7scb9xec09lt1y3mp0lqllv38 하나뿐임을 확인할 수 있습니다. 이 인덱스는 concert_id 컬럼에만 적용된 외래 키 기반 인덱스로 추정되며
, 나머지 조건인 zone과 reservation_status에 대해서는 테이블 레벨에서 WHERE 절을 통해 필터링을 수행하고 있습니다.

이는 MySQL이 세 가지 조건 중 오직 concert_id에 대해서만 인덱스를 활용하고, 나머지 필터 조건은 인덱스를 활용하지 못하고 있다는 것을 의미합니다. 즉, 조건이 완전히 인덱스를 타지 않기 때문에 불필요한 I/O가 발생하고, 성능 저하로 이어질 수 있다고 생각합니다.

type: ref는 MySQL 옵티마이저가 인덱스를 사용하고 있다는 것을 의미하긴 하지만, 이는 흔히 단일 인덱스 키에 대해서만 동등 비교(equality match)가 일어날 때 발생합니다. 이 경우에도 결국 추가 조건에 대해서는 row-level filtering이 필요하기 때문에, 전체적으로 효율적인 인덱스 사용이라고 보기 어렵습니다.

특히 zone, concert_id, reservation_status 세 필드는 자주 함께 조회되는 컬럼들이고, 결합된 형태의 조건절로 쓰이는 경우가 많다면, 해당 컬럼들을 모두 포함하는 복합 인덱스를 구성하는 것이 쿼리 성능 최적화에 매우 중요하다고 생각해서 복합인덱스를 구성해봤습니다.

CREATE INDEX idx_seats_zone_concert_status
ON seats (zone, concert_id, reservation_status);

이 인덱스를 생성하면 MySQL 옵티마이저는 세 조건을 모두 만족하는 인덱스를 통해 레코드를 탐색할 수 있게 되며, 불필요한 테이블 스캔 없이도 필요한 데이터만 효율적으로 조회할 수 있습니다.

개선후 Explain
1	SIMPLE	seats		ref	idx_zone_concert_reservation	idx_zone_concert_reservation	13	const,const,const	9	100.0	Using index condition

복합 인덱스를 적용한 덕분에 세 가지 조건(concert_id, zone, reservation_status)을 한 번에 인덱스로 조회할 수 있어 쿼리 성능이 개선되었습니다. 테이블 전체를 탐색하지 않아도 되고, 불필요한 디스크 I/O도 줄어들었습니다.!!

@Table(name = "seats",
indexes = {
@Index(name = "idx_zone_concert_reservation", columnList = "concert_id, zone, reservation_status")
})
public class SeatEntity {


- [ ] 테이블 구조 개선안 (선택)
- [ ] Infrastructure Layer 구성
- [ ] 기능별 통합 테스트

#### STEP08
- [ ] 조회 성능 저하 기능 식별
- [ ] 쿼리 실행계획 기반 문제 분석
- [ ] 인덱스/쿼리 재설계 및 개선안 도출




### **간단 회고** (3줄 이내)
- **잘한 점**:
- **어려운 점**:
- **다음 시도**: