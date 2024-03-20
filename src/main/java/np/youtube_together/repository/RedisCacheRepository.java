package np.youtube_together.repository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import np.youtube_together.dto.chatting.ChattingRoomDto;
import np.youtube_together.redis.RedisSubscriber;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class RedisCacheRepository {

    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisSubscriber redisSubscriber;


    private final RedisTemplate<String, Object> redisTemplate;
    private static final String Room = "ChattingRoom";
    private HashOperations<String, String, ChattingRoomDto> opsHashChattingRoom;
    //public interface HashOperations<H,HK,HV>  해쉬 이름, 키, 밸류
    private Map<String, ChannelTopic> topics;



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
        return chattingRoomDto;
    }

    public void enterChattingRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);
        if(topic == null)
            topic = new ChannelTopic(roomId);
        redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);
        topics.put(roomId, topic);
    }

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }

}
