package SenierProject.BlockDeal.chat;

import lombok.Data;

@Data
public class ChatRequestDto {
    private Long roomId;      // 채팅방 ID
    private String message;   // 메시지 내용
    private String recipient; // 수신자
}