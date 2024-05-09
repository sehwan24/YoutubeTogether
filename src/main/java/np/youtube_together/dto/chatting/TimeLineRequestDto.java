package np.youtube_together.dto.chatting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimeLineRequestDto {

    private String videoId;
    private String timeLine;
}
