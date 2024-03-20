package np.youtube_together.repository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import np.youtube_together.dto.chatting.ChattingRoomDto;
import np.youtube_together.dto.chatting.MessageDto;
import np.youtube_together.redis.RedisSubscriber;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class RedisCacheRepository {

    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisSubscriber redisSubscriber;


    private final RedisTemplate<String, Object> redisTemplate;
    private static final String Room = "ChattingRoomDto";
    private HashOperations<String, String, ChattingRoomDto> opsHashChattingRoom;
    //public interface HashOperations<H,HK,HV>  해쉬 이름, 키, 밸류
    private Map<String, ChannelTopic> topics;

    private static final String RECENT_MESSAGES_KEY = "RECENT_SENDERS"; // 리스트의 이름 변경
    private static final int MAX_LIST_SIZE = 2; // 리스트의 최대 크기




    @PostConstruct
    private void init() {
        opsHashChattingRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }
    public List<ChattingRoomDto> findAllRooms() {
        return opsHashChattingRoom.values(Room);
    }

    //채팅방 Redis에 저장 근데 Dto를 저장해야할듯
    public ChattingRoomDto createChattingRoom(String name) {
        ChattingRoomDto chattingRoomDto = ChattingRoomDto.create(name);
        opsHashChattingRoom.put(Room, chattingRoomDto.getRoomId(), chattingRoomDto); //직렬화한 값을 저장해야함
        System.out.println("chattingRoomDto = " + chattingRoomDto);
        return chattingRoomDto;
    }

    //enterChattingRoom부터 ㄱㄱ
    public void enterChattingRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);
        if(topic == null)
            topic = new ChannelTopic(roomId);
        redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);
        topics.put(roomId, topic);
    }




    public List<String> getSenderFromRedis() {
        // 리스트의 모든 요소를 가져오기
        List<Object> rawValues = redisTemplate.opsForList().range(RECENT_MESSAGES_KEY, 0, -1);
        List<String> stringValues = rawValues.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
        return stringValues;
    }





    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }

}
