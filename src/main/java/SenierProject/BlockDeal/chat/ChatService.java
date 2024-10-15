package SenierProject.BlockDeal.chat;

import SenierProject.BlockDeal.chat.chatdto.ChatMessageDto;
import SenierProject.BlockDeal.chat.chatdto.ChatRoomDto;
import SenierProject.BlockDeal.chat.chatdto.MessageType;
import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.exception.ChatRoomNotFoundException;
import SenierProject.BlockDeal.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberService memberService;

    @Transactional
    public ChatRoom createOrGetChatRoom(Long buyerId, Long sellerId, String roomName) {
        Member buyer = memberService.findById(buyerId);
        Member seller = memberService.findById(sellerId);

        Optional<ChatRoom> existingRoom = chatRoomRepository.findByUser1AndUser2(buyer, seller);
        if (existingRoom.isEmpty()) {
            existingRoom = chatRoomRepository.findByUser1AndUser2(seller, buyer);
        }

        return existingRoom.orElseGet(() -> chatRoomRepository.save(new ChatRoom(roomName, buyer, seller)));
    }

    @Transactional
    public ChatMessageDto sendMessage(Long roomId, Member sender, String content) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("존재하지 않는 채팅방입니다. ID: " + roomId));

        if (!isParticipant(chatRoom, sender)) {
            throw new IllegalArgumentException("해당 채팅방에 메시지를 전송할 권한이 없습니다.");
        }

        // ChatMessage 생성 시 MessageType을 설정하고 sender를 필드에 포함
        ChatMessage chatMessage = new ChatMessage(
                chatRoom,
                sender,
                content,
                MessageType.TALK,  // TALK 타입 설정
                LocalDateTime.now() // 현재 시간 설정
        );
        chatMessageRepository.save(chatMessage);

        return new ChatMessageDto(
                roomId,
                content,
                sender.getUsername(),
                LocalDateTime.now(),
                MessageType.TALK
        );
    }

    private boolean isParticipant(ChatRoom chatRoom, Member sender) {
        return chatRoom.getUser1().equals(sender) || chatRoom.getUser2().equals(sender);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDto> getMessagesByRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("존재하지 않는 채팅방입니다. ID: " + roomId));

        return chatMessageRepository.findByChatRoom(chatRoom)
                .stream()
                .map(message -> new ChatMessageDto(
                        chatRoom.getId(),
                        message.getContent(),
                        message.getSender().getUsername(),
                        message.getSentAt(),
                        message.getType()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChatRoomDto> getChatRoomsForUser(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("잘못된 사용자 ID입니다.");
        }

        List<ChatRoom> chatRooms = chatRoomRepository.findAllByUser1IdOrUser2Id(userId, userId);

        if (chatRooms.isEmpty()) {
            throw new ChatRoomNotFoundException("사용자가 참여한 채팅방이 없습니다.");
        }

        return chatRooms.stream()
                .map(ChatRoomDto::new)
                .collect(Collectors.toList());
    }
}
