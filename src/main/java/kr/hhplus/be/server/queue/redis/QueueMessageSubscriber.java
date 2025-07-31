package kr.hhplus.be.server.queue.redis;

import lombok.Getter;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class QueueMessageSubscriber implements MessageListener {
    private final List<String> receivedMessages = new ArrayList<>();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        System.out.println("[Redis] 채널 " + channel + " 에서 메시지 수신: " + body);
        receivedMessages.add(message.toString());
    }

}
