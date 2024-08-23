package SenierProject.BlockDeal.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate template;

    // WebSocket - 1:1 메시지 전송
    @MessageMapping("/chat/send")
    public void sendMessageViaWebSocket(ChatRequestDto messageDto, Authentication authentication) {
        String sender = authentication.getName(); // JWT에서 사용자 이름 추출

        // 메시지 전송 및 저장
        ChatMessage chatMessage = chatService.sendMessage(sender, messageDto.getRoomId(), messageDto.getMessage());

        // 메시지 전송 (수신자의 개인 큐로 전송)
        template.convertAndSendToUser(
                messageDto.getRecipient(),  // 수신자
                "/sub/chat/" + messageDto.getRoomId(),
                chatMessage
        );
    }

    // WebSocket - 채팅방 입장 알림
    @MessageMapping("/chat/enter")
    public void enterChatRoom(ChatRequestDto messageDto, Authentication authentication) {
        String sender = authentication.getName();

        // ChatRoom 가져오기
        ChatRoom chatRoom = chatService.findChatRoomById(messageDto.getRoomId());

        // 입장 메시지 생성 및 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .sender(sender)
                .content(sender + "님이 채팅방에 참여하였습니다.")
                .timestamp(LocalDateTime.now())
                .chatRoom(chatRoom)
                .build();

        chatService.sendMessage(sender, messageDto.getRoomId(), chatMessage.getContent());

        // 입장 메시지 전송 (채팅방의 모든 구독자에게 전송)
        template.convertAndSend("/sub/chat/" + chatRoom.getId(), chatMessage);
    }
}