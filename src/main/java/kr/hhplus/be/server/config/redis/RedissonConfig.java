package kr.hhplus.be.server.config.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@Lazy
public class RedissonConfig {

    private static final String REDISSON_HOST_PREFIX = "redis://";
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;
    @Bean
    public RedissonClient  redissonClient() {
        Config config = new Config();
        config.useSingleServer()  // 싱글 Redis 서버 모드 사용
                .setAddress(REDISSON_HOST_PREFIX + redisHost + ":" + redisPort)
                .setConnectionMinimumIdleSize(5) // 최소 유휴 연결 수
                .setConnectionPoolSize(10)// 최대 커넥션 풀 크기
                .setIdleConnectionTimeout(100000) // 유휴 연결 타임아웃 (ms)
                .setConnectTimeout(3000) // 연결 타임아웃 (ms)
                .setTimeout(3000)  // 명령 실행 타임아웃 (ms)
                .setRetryAttempts(3);   // 재시도 횟수
        return Redisson.create(config);
    }

}