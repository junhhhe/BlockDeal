package SenierProject.BlockDeal.chat;

import SenierProject.BlockDeal.chat.chatdto.ChatCreateRequestDto;
import SenierProject.BlockDeal.chat.chatdto.ChatMessageDto;
import SenierProject.BlockDeal.chat.chatdto.ChatRoomDto;
import SenierProject.BlockDeal.dto.ApiResponse;
import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final MemberService memberService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 구매자와 판매자 간의 채팅방 생성 요청
     * 이미 존재하는 채팅방이 있을 경우 해당 채팅방을 반환
     * 없을 경우 새로운 채팅방을 생성 후 반환
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ChatRoom>> createOrFindChatRoom(@RequestBody ChatCreateRequestDto chatCreateRequestDto) {
        Long buyerId = chatCreateRequestDto.getBuyerId();
        Long sellerId = chatCreateRequestDto.getSellerId();
        String roomName = chatCreateRequestDto.getRoomName();

        // Member 객체 조회
        Member buyer = memberService.findById(buyerId);
        Member seller = memberService.findById(sellerId);

        log.info("채팅방 생성 요청 - buyerId: {}, sellerId: {}, roomName: {}", buyerId, sellerId, roomName);

        // 서비스 레이어에서 채팅방 생성 또는 조회
        ChatRoom chatRoom = chatService.createOrGetChatRoom(buyer, seller, roomName);

        return ResponseEntity.ok(new ApiResponse<>(true, "채팅방이 생성되었거나 기존 채팅방을 반환합니다.", chatRoom));
    }

    /**
     * STOMP를 통한 메시지 전송
     */
    @MessageMapping("/send")
    @SendTo("/sub/messages")
    public ChatMessage sendMessage(@Payload ChatMessageDto messageDto, Principal principal) {
        if (principal == null) {
            log.error("인증 정보가 유효하지 않습니다. Principal 객체가 null입니다.");
            throw new IllegalStateException("인증 정보가 유효하지 않습니다.");
        }

        log.info("Principal 정보: {}", principal);
        String username = principal.getName();

        // Principal을 Member로 캐스팅하거나 DB에서 사용자 조회
        Member sender = memberService.getUserByUsername(username);
        if (sender == null) {
            log.error("사용자 정보를 찾을 수 없습니다. username: {}", username);
            throw new IllegalStateException("사용자 정보를 찾을 수 없습니다.");
        }

        log.info("메시지 전송 - sender: {}, chatRoomId: {}, content: {}", sender.getUsername(), messageDto.getChatRoomId(), messageDto.getContent());

        // 메시지 전송
        return chatService.sendMessage(messageDto.getChatRoomId(), sender, messageDto.getContent());
    }


    /**
     * 특정 채팅방의 모든 메시지 조회
     */
    @GetMapping("/messages")
    public ResponseEntity<ApiResponse<List<ChatMessageDto>>> getMessagesByRoom(@RequestParam Long roomId) {
        log.info("채팅방 메시지 조회 - roomId: {}", roomId);

        // 서비스 레이어를 통해 메시지 목록 조회
        List<ChatMessageDto> messages = chatService.getMessagesByRoom(roomId);
        return ResponseEntity.ok(new ApiResponse<>(true, "메시지 목록 조회 성공", messages));
    }

    /**
     * 사용자가 참여한 모든 채팅방 목록 조회
     * - ChatRoomDto 형식으로 반환하여 사용자 이름을 포함한 채팅방 정보를 제공
     */
    @GetMapping("/rooms")
    public ResponseEntity<ApiResponse<List<ChatRoomDto>>> getChatRooms(Principal principal) {
        try {
            // 현재 인증된 사용자 ID 가져오기
            String username = principal.getName();
            Member member = memberService.getUserByUsername(username);

            // 사용자가 참여한 채팅방 목록 가져오기 (ChatRoom 리스트)
            List<ChatRoomDto> chatRooms = chatService.getChatRoomsForUser(member.getId());

            return ResponseEntity.ok(new ApiResponse<>(true, "참여한 채팅방 목록 조회 성공", chatRooms));
        } catch (Exception ex) {
            log.error("채팅방 목록 조회 중 오류 발생: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "채팅방 목록 조회 중 오류가 발생했습니다.", null));
        }
    }
}
