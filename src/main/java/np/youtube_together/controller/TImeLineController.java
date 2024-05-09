package np.youtube_together.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import np.youtube_together.dto.chatting.TimeLineDto;
import np.youtube_together.dto.chatting.TimeLineRequestDto;
import np.youtube_together.repository.RedisCacheRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/timeline")
public class TImeLineController {

    private final RedisCacheRepository redisCacheRepository;

    @PostMapping("/new")
    public TimeLineDto createTimeLine(@RequestBody TimeLineRequestDto timeLineRequestDto) {
        return redisCacheRepository.createTimeLine(timeLineRequestDto.getVideoId(), timeLineRequestDto.getTimeLine());
    }

    @GetMapping("/{videoId}")
    public List<String> findRoomTimeLineByVideoId(@PathVariable("videoId") String videoId) {
        return redisCacheRepository.findRoomTimeLineByVideoId(videoId);
    }
}
