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

        System.out.println("messageDto = " + messageDto.getMessage());

        System.out.println("cache_data = " + redisCacheRepository.getSenderFromRedis());

        messageDto.setCount(Integer.parseInt(redisCacheRepository.getSenderFromRedis()) + 1);

        //redisPublisher.publish(redisCacheRepository.getTopic(messageDto.getRoomId()), messageDto);

        if (messageDto.getCount() == 1) {
            //4의 배수는 또 통과 시켜
            redisPublisher.publish(redisCacheRepository.getTopic(messageDto.getRoomId()), messageDto);
        } else if (messageDto.getCount() == 3) {
            redisPublisher.publish(redisCacheRepository.getTopic(messageDto.getRoomId()), messageDto);
        } else if (messageDto.getCount() == 6) {
            redisPublisher.publish(redisCacheRepository.getTopic(messageDto.getRoomId()), messageDto);
        } else {
            System.out.println("messageDto.getMessage() = " + messageDto.getMessage());
            redisCacheRepository.plusCount();
            System.out.println("cache_data2 = " + redisCacheRepository.getSenderFromRedis());
        }

    }

}
