package SenierProject.BlockDeal.chat.chatdto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatMessageDto {
    private Long chatRoomId;
    private String content;
    private String senderName; // 메시지 보낸 사람 이름
    private LocalDateTime sentAt; // 메시지 전송 시간
    private MessageType type; // 메시지 타입 (ENTER, TALK, EXIT 등)
}