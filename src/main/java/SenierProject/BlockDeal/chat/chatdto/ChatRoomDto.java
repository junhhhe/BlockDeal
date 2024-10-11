package SenierProject.BlockDeal.chat.chatdto;

import SenierProject.BlockDeal.chat.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {

    private Long id;
    private String roomName;
    private String user1;  // 첫 번째 참여자 이름 (username)
    private String user2;  // 두 번째 참여자 이름 (username)

    // ChatRoom 객체를 인자로 받는 생성자
    public ChatRoomDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.roomName = chatRoom.getRoomName();
        this.user1 = chatRoom.getUser1().getUsername();  // Member 객체의 username 사용
        this.user2 = chatRoom.getUser2().getUsername();  // Member 객체의 username 사용
    }
}