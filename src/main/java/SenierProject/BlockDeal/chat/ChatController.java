package SenierProject.BlockDeal.chat;

import SenierProject.BlockDeal.chat.chatdto.ChatCreateRequestDto;
import SenierProject.BlockDeal.chat.chatdto.ChatMessageDto;
import SenierProject.BlockDeal.chat.chatdto.ChatRoomDto;
import SenierProject.BlockDeal.dto.ApiResponse;
import SenierProject.BlockDeal.dto.CustomUserDetails;
import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final MemberService memberService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ChatRoomDto>> createOrFindChatRoom(@RequestBody ChatCreateRequestDto request) {
        ChatRoom chatRoom = chatService.createOrGetChatRoom(
                request.getBuyerId(), request.getSellerId(), request.getRoomName());
        return ResponseEntity.ok(new ApiResponse<>(true, "채팅방 생성 완료", new ChatRoomDto(chatRoom)));
    }

    @GetMapping("/rooms")
    public ResponseEntity<ApiResponse<List<ChatRoomDto>>> getChatRooms() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "사용자 정보가 없습니다. 로그인을 해주세요.", null));
            }

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            log.info("현재 사용자 이름: {}", userDetails.getUsername());

            Member member = userDetails.getMember();
            List<ChatRoomDto> chatRooms = chatService.getChatRoomsForUser(member.getId());

            return ResponseEntity.ok(new ApiResponse<>(true, "참여한 채팅방 목록 조회 성공", chatRooms));
        } catch (Exception ex) {
            log.error("채팅방 목록 조회 중 오류 발생: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "채팅방 목록 조회 중 오류가 발생했습니다.", null));
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<ApiResponse<List<ChatMessageDto>>> getMessagesByRoom(@RequestParam Long roomId) {
        List<ChatMessageDto> messages = chatService.getMessagesByRoom(roomId);
        return ResponseEntity.ok(new ApiResponse<>(true, "메시지 목록 조회 성공", messages));
    }

    @MessageMapping("/send/{chatRoomId}")
    @SendTo("/sub/messages/{chatRoomId}")
    public ChatMessageDto sendMessage(
            @DestinationVariable Long chatRoomId,
            @Payload ChatMessageDto messageDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("인증되지 않은 사용자입니다.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info("메시지 발신자: {}", userDetails.getUsername());

        return chatService.sendMessage(chatRoomId, userDetails.getMember(), messageDto.getContent());
    }
}
