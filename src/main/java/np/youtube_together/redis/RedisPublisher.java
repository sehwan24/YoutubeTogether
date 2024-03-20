package np.youtube_together.redis;


import lombok.RequiredArgsConstructor;
import np.youtube_together.dto.chatting.MessageDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, MessageDto messageDto) {
        System.out.println("messageDto = " + messageDto);
        redisTemplate.convertAndSend(topic.getTopic(), messageDto);
    }

}
