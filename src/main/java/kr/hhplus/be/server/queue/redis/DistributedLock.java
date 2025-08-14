package kr.hhplus.be.server.queue.redis;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {
    String key(); // 락 식별 Redis Key

    long waitTime() default 5; // 락 획득 대기시간

    long leaseTime() default 10; // 락 점유시간
}
