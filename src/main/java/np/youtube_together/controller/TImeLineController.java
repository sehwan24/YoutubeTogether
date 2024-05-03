package np.youtube_together.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import np.youtube_together.dto.chatting.TimeLineDto;
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
    public TimeLineDto createTimeLine(@RequestBody String videoId, @RequestBody String timeLine) {
        return redisCacheRepository.createTimeLine(videoId, timeLine);
    }

    @GetMapping("/{videoId}")
    public TimeLineDto findRoomTimeLineByVideoId(@PathVariable("videoId") String videoId) {
        return redisCacheRepository.findRoomTimeLineByVideoId(videoId);
    }
}
