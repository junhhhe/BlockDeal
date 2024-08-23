package SenierProject.BlockDeal.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponseDto {
    private Long messageId;       // 메시지의 고유 ID (DB에서 생성된 경우)
    private Long roomId;        // 채팅방 ID
    private String sender;        // 메시지 전송자
    private String message;       // 메시지 내용
    private LocalDateTime timestamp; // 메시지 전송 시간
}