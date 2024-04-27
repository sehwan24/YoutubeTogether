package np.youtube_together.controller;

import lombok.RequiredArgsConstructor;
import np.youtube_together.dto.chatting.MessageDto;
import np.youtube_together.redis.RedisPublisher;
import np.youtube_together.repository.RedisCacheRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MessageController {

    private final RedisPublisher redisPublisher;
    private final RedisCacheRepository redisCacheRepository;

    @MessageMapping("/chatting/message")
    public void message(MessageDto messageDto) {
        if (MessageDto.MessageType.ENTER.equals(messageDto.getType())) {
            redisCacheRepository.enterChattingRoom(messageDto.getRoomId());
            messageDto.setMessage(messageDto.getSender() + "님이 입장하셨습니다.");
        }

        redisPublisher.publish(redisCacheRepository.getTopic(messageDto.getRoomId()), messageDto);
    }

}
