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
            System.out.println("messageDto = " + messageDto);
        }

        System.out.println("Hi");

        System.out.println(redisCacheRepository.getSenderFromRedis());
        System.out.println("messageDto.getSender() = " + messageDto.getSender());

        List<String> senderFromRedis = redisCacheRepository.getSenderFromRedis();

        if (senderFromRedis.get(0) == messageDto.getSender()) {
            redisPublisher.publish(redisCacheRepository.getTopic(messageDto.getRoomId()), messageDto);
        }

    }

}
