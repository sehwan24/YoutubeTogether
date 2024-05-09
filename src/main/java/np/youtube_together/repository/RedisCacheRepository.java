package np.youtube_together.repository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import np.youtube_together.dto.chatting.ChattingRoomDto;
import np.youtube_together.dto.chatting.MessageDto;
import np.youtube_together.dto.chatting.TimeLineDto;
import np.youtube_together.redis.RedisSubscriber;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
    private static final String TimeLine = "TimeLineDto";
    private HashOperations<String, String, ChattingRoomDto> opsHashChattingRoom;
    private HashOperations<String, String, TimeLineDto> opsHashTimeLine;
    //public interface HashOperations<H,HK,HV>  해쉬 이름, 키, 밸류
    private Map<String, ChannelTopic> topics;





    @PostConstruct
    private void init() {
        opsHashChattingRoom = redisTemplate.opsForHash();
        opsHashTimeLine = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }
    public List<ChattingRoomDto> findAllRooms() {
        return opsHashChattingRoom.values(Room);
    }
    public List<TimeLineDto> findAllTimeLines() {
        return opsHashTimeLine.values(TimeLine);
    }

    public List<String> findRoomTimeLineByVideoId(String videoId) {
        System.out.println("opsHashTimeLine = " + opsHashTimeLine.get(TimeLine, videoId).getTimeLines());
        return opsHashTimeLine.get(TimeLine, videoId).getTimeLines();
    }

    //채팅방 Redis에 저장 근데 Dto를 저장해야할듯
    public ChattingRoomDto createChattingRoom(String name) {
        ChattingRoomDto chattingRoomDto = ChattingRoomDto.create(name);
        opsHashChattingRoom.put(Room, chattingRoomDto.getRoomId(), chattingRoomDto); //직렬화한 값을 저장해야함
        System.out.println("chattingRoomDto = " + chattingRoomDto);
        return chattingRoomDto;
    }

    //todo : 타임라인하나씩 올때마다 레디스에 저장. 확인로직 필요 !! 저장되어있는 채팅방인지..?
    public TimeLineDto createTimeLine(String videoId, String timeLine) {
        if (opsHashTimeLine.get(TimeLine, videoId) != null) { //타임라인 있는 비디오 일때
            TimeLineDto timeLineDto = opsHashTimeLine.get(TimeLine, videoId);
            List<String> timeLines = timeLineDto.getTimeLines();
            timeLines.add(timeLine);
            TimeLineDto updateTimeLineDto = TimeLineDto.create(videoId, timeLines);
            opsHashTimeLine.put(TimeLine, videoId, updateTimeLineDto);
            return updateTimeLineDto;
        } else {
            List<String> timeLines = new ArrayList<>();
            timeLines.add(timeLine);
            TimeLineDto timeLineDto = TimeLineDto.create(videoId, timeLines);
            opsHashTimeLine.put(TimeLine, videoId, timeLineDto);
            return timeLineDto;
        }
    }

    //enterChattingRoom부터 ㄱㄱ
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
