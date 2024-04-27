package np.youtube_together.dto.chatting;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {

    public enum MessageType {
        ENTER, TALK, VIDEO
    }

    public enum RunningType {
        RUN, STOP, NO
    }

    private MessageType type;
    private RunningType runningType;
    private String roomId;
    private String sender;
    private String message;

}
