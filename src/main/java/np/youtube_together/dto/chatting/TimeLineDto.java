package np.youtube_together.dto.chatting;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TimeLineDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -6407130708589775870L;

    //private String timeLineId;
    private String videoId;
    private List<String> timeLines;

    public static TimeLineDto create(String videoId, List<String> timeLines) {
        TimeLineDto timeLineDto = new TimeLineDto();
        //timeLineDto.timeLineId = UUID.randomUUID().toString();
        timeLineDto.videoId = videoId;
        timeLineDto.timeLines = timeLines;
        return timeLineDto;
    }

}
