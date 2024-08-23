package SenierProject.BlockDeal.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequestDto {
    private Long roomId;        // 채팅방 ID
    private String sender;        // 메시지 전송자 (서버에서 설정)
    private String recipient;   // 메시지 수신자
    private String message;       // 메시지 내용
    private LocalDateTime timestamp; // 메시지 전송 시간 (서버에서 설정)
}