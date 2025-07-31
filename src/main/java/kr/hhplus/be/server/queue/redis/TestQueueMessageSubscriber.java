package kr.hhplus.be.server.queue.redis;

import lombok.Getter;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

    @Getter
    @Component
    @Profile("test")
    public class TestQueueMessageSubscriber implements MessageListener {

        private final List<String> receivedMessages = new ArrayList<>();

        @Override
        public void onMessage(Message message, byte[] pattern) {
            receivedMessages.add(message.toString());
        }

}