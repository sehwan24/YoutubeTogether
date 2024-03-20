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
            // 리스트의 현재 길이 확인
            Long listSize = redisTemplate.opsForList().size(RECENT_MESSAGES_KEY);
            // 리스트의 길이가 최대 크기보다 큰 경우, 왼쪽 끝에서 요소를 제거
            if (listSize != null && listSize >= MAX_LIST_SIZE) {
                redisTemplate.opsForList().leftPop(RECENT_MESSAGES_KEY);
            }
            // 새로운 sender를 리스트의 오른쪽 끝에 추가
            redisTemplate.opsForList().rightPush(RECENT_MESSAGES_KEY, roomMessage.getSender());
            simpMessageSendingOperations.convertAndSend("/sub/chatting/room/" + roomMessage.getRoomId(), roomMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
