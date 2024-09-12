package SenierProject.BlockDeal.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate template;

    // 1. 채팅방 생성 또는 기존 채팅방 반환
    @PostMapping("/room")
    public ChatRoom createOrGetChatRoom(@RequestParam String username1, @RequestParam String username2) {
        return chatService.createOrGetChatRoom(username1, username2);
    }

    // 2. 특정 채팅방의 모든 메시지 조회
    @GetMapping("/room/{roomId}/messages")
    public List<ChatMessage> getMessagesByChatRoom(@PathVariable Long roomId) {
        return chatService.getMessagesByChatRoom(roomId);
    }

    // 3. 특정 사용자가 참여한 모든 채팅방 조회
    @GetMapping("/rooms")
    public List<ChatRoom> getAllChatRoomsForUser(Authentication authentication) {
        String username = authentication.getName();  // JWT를 통해 인증된 사용자 이름 가져옴
        return chatService.getAllChatRoomsForUser(username);
    }

    // 4. 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    public ChatRoom getChatRoomById(@PathVariable Long roomId) {
        return chatService.getChatRoomById(roomId);
    }

    // 5. 메시지 전송 (REST API 방식)
    @PostMapping("/room/{roomId}/message")
    public ChatMessage sendMessage(@PathVariable Long roomId,
                                   @RequestParam String message,
                                   Authentication authentication) {
        String senderUsername = authentication.getName();  // JWT에서 사용자 이름 추출
        return chatService.sendMessage(senderUsername, roomId, message);
    }

    // 6. 메시지 전송 (WebSocket 방식)
    @MessageMapping("/send")
    public void sendMessageViaWebSocket(ChatRequestDto messageDto, Authentication authentication) {
        String sender = authentication.getName();  // JWT에서 사용자 이름 추출

        // 메시지 전송 및 저장
        ChatMessage chatMessage = chatService.sendMessage(sender, messageDto.getRoomId(), messageDto.getMessage());

        // WebSocket을 통한 메시지 전송 (수신자의 개인 큐로 전송)
        template.convertAndSendToUser(
                messageDto.getRecipient(),  // 수신자
                "/sub/chat/" + messageDto.getRoomId(),
                chatMessage
        );
    }

    // 7. 채팅방 입장 알림 (WebSocket 방식)
    @MessageMapping("/enter")
    public void enterChatRoom(ChatRequestDto messageDto, Authentication authentication) {
        String senderUsername = authentication.getName();

        // ChatRoom 가져오기
        ChatRoom chatRoom = chatService.getChatRoomById(messageDto.getRoomId());

        // 입장 메시지 생성 및 저장
        ChatMessage chatMessage = chatService.sendMessage(senderUsername, messageDto.getRoomId(), senderUsername + "님이 채팅방에 참여하였습니다.");

        // WebSocket을 통한 입장 알림 전송
        template.convertAndSend("/sub/chat/" + chatRoom.getId(), chatMessage);
    }
}
