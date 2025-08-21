package kr.hhplus.be.server.queue.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisPubSubConfig {

    // 1 RedisListenerContainer 가 Redis 에 연결
    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // 2 queue:log 채널을 구독 → 메시지 오면 listenerAdapter 에게 전달
        container.addMessageListener(listenerAdapter, new ChannelTopic("queue:log"));

        return container;
    }

    // 3 listenerAdapter 는 실제 subscriber 객체와 연결되어 있음
    @Bean
    @Profile("local")
    public MessageListenerAdapter listenerAdapter(QueueMessageSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber);
    }

    @Bean
    @Profile("test")
    public MessageListenerAdapter testListenerAdapter(TestQueueMessageSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber);
    }
}
