package SenierProject.BlockDeal.chat.chatdto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatCreateRequestDto {

    private String roomName;
    private Long sellerId; // 판매자의 ID
    private Long buyerId;
}