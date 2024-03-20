package np.youtube_together.dto.chatting;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class ChattingRoomDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -6407130708589775870L;


    private String roomId;
    private String roomName;


    public static ChattingRoomDto create(String name) {
        ChattingRoomDto chattingRoomDto = new ChattingRoomDto();
        chattingRoomDto.roomId = UUID.randomUUID().toString();
        chattingRoomDto.roomName = name;
        return chattingRoomDto;
    }

}
