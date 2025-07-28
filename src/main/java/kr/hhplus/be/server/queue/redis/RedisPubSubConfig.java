package kr.hhplus.be.server.queue.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisPubSubConfig {

    // ① RedisListenerContainer가 Redis에 연결
    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // ② queue:log 채널을 구독 → 메시지 오면 listenerAdapter에게 전달
        container.addMessageListener(listenerAdapter, new ChannelTopic("queue:log"));

        return container;
    }

    // ③ listenerAdapter는 실제 subscriber 객체와 연결되어 있음
    @Bean
    public MessageListenerAdapter listenerAdapter(QueueMessageSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber);
    }
}
