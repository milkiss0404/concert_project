package kr.hhplus.be.server.queue.repository;

import kr.hhplus.be.server.config.CustomTestContainer;
import kr.hhplus.be.server.queue.jwt.JwtTokenProvider;
import kr.hhplus.be.server.queue.redis.TestQueueMessageSubscriber;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@DisplayName("Redis infrastructure 레이어 테스트")
@ActiveProfiles("test")
@Nested
class RedisQueueRepositoryImplTest extends CustomTestContainer {

    @Autowired
    private QueueRepository queueRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private TestQueueMessageSubscriber queueMessageSubscriber;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String TOKEN_KEY_PREFIX = "queue:token:";
    private static final String QUEUE_KEY_PREFIX = "concert:queue:";



    private Long userId;
    private Long concertId;

    @BeforeEach
    void setup() {
         userId = 1L;
         concertId = 1L;
    }

    @Test
    @DisplayName("Redis에 이벤트 발행 시 구독자가 메시지를 수신한다")
    void publishJoinEvent() {
        // given
        // when
        queueRepository.publishJoinEvent(concertId, userId);

        // then
        String expectedMessage = "User " + userId + " joined Queue " + concertId;
        await().atMost(3, TimeUnit.SECONDS).untilAsserted(() ->
                assertThat(queueMessageSubscriber.getReceivedMessages())
                        .contains(expectedMessage));

    }

    @Test
    @DisplayName("jwt 토큰 저장")
    void saveToken() {
        // given
        String jwtToken = jwtTokenProvider.createJwtToken(userId, concertId);
        // when
        queueRepository.saveToken(jwtToken, userId);

        // then
        String savedUserId = redisTemplate.opsForValue().get(TOKEN_KEY_PREFIX + jwtToken);

        assertThat(savedUserId).isNotEmpty();
        assertThat(savedUserId).contains(userId.toString());
    }

    @Test
    @DisplayName("pushToQueue 검증")
    void pushToQueue() {
        // given
        // when
        queueRepository.pushToQueue(concertId,userId);
        // then
        String userIdQueue = redisTemplate.opsForList().leftPop(QUEUE_KEY_PREFIX+concertId);
        Assertions.assertThat(userIdQueue).isEqualTo(userId.toString());
    }


}