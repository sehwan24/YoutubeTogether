package np.youtube_together.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import np.youtube_together.dto.chatting.ChattingRoomDto;
import np.youtube_together.repository.RedisCacheRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chatting")
public class ChattingRoomController {

    private final RedisCacheRepository redisCacheRepository;

    @GetMapping("/rooms")
    public List<ChattingRoomDto> chattingRooms() {
        return redisCacheRepository.findAllRooms();
    }


    @PostMapping("/room")
    public ChattingRoomDto createRoom(@RequestBody String name) {
        return redisCacheRepository.createChattingRoom(name);
    }

}
