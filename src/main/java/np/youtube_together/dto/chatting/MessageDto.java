package np.youtube_together.dto.chatting;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {

    public enum MessageType {
        ENTER, TALK
    }

    public enum RunningType {
        RUN, STOP
    }

    private MessageType type;
    private RunningType runningType;
    private int count;
    private String roomId;
    private String sender;
    private String message;

}
