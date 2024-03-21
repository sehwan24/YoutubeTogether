package np.youtube_together.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import np.youtube_together.dto.chatting.MessageDto;
import np.youtube_together.repository.RedisCacheRepository;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private static final String RECENT_MESSAGES_KEY = "RECENT_SENDERS"; // 리스트의 이름 변경
    private static final int MAX_LIST_SIZE = 2;



    @Override
    public void onMessage(org.springframework.data.redis.connection.Message message, byte[] pattern) {
        try {
            System.out.println("message = " + message);
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            MessageDto roomMessage = objectMapper.readValue(publishMessage, MessageDto.class);
            redisTemplate.opsForValue().set("COUNT_KEY", roomMessage.getCount());
            simpMessageSendingOperations.convertAndSend("/sub/chatting/room/" + roomMessage.getRoomId(), roomMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
