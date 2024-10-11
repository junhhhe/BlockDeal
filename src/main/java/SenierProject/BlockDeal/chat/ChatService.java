package SenierProject.BlockDeal.chat;

import SenierProject.BlockDeal.chat.chatdto.ChatMessageDto;
import SenierProject.BlockDeal.chat.chatdto.ChatRoomDto;
import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.exception.ChatRoomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    /**
     * 구매자와 판매자 간의 채팅방을 생성하거나 기존 채팅방을 조회하는 메서드
     * - 이미 채팅방이 존재할 경우 해당 채팅방을 반환
     * - 채팅방이 없을 경우 새로 생성한 후 반환
     */
    @Transactional
    public ChatRoom createOrGetChatRoom(Member buyer, Member seller, String roomName) {
        // 1. 구매자와 판매자가 이미 참여한 채팅방이 있는지 확인
        Optional<ChatRoom> existingRoom = chatRoomRepository.findByUser1AndUser2(buyer, seller);

        if (existingRoom.isPresent()) {
            return existingRoom.get(); // 기존 채팅방 반환
        }

        // 2. 채팅방이 존재하지 않으면 새로운 채팅방 생성 및 저장
        ChatRoom newChatRoom = new ChatRoom(roomName, buyer, seller);
        return chatRoomRepository.save(newChatRoom);
    }

    /**
     * 특정 채팅방에 메시지를 전송하는 메서드
     * - 채팅방의 참여자가 아니면 메시지를 보낼 수 없음
     */
    @Transactional
    public ChatMessage sendMessage(Long roomId, Member sender, String content) {
        // 1. 채팅방 조회 및 존재 여부 확인
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("존재하지 않는 채팅방입니다. ID: " + roomId));

        // 2. 메시지를 보낸 사용자가 해당 채팅방의 참여자인지 확인
        if (!chatRoom.getUser1().equals(sender) && !chatRoom.getUser2().equals(sender)) {
            throw new IllegalArgumentException("해당 채팅방에 메시지를 전송할 권한이 없습니다.");
        }

        // 3. 메시지 생성 및 저장
        ChatMessage chatMessage = new ChatMessage(chatRoom, sender, content);
        return chatMessageRepository.save(chatMessage);
    }

    /**
     * 특정 채팅방의 모든 메시지를 조회하는 메서드
     */
    public List<ChatMessageDto> getMessagesByRoom(Long roomId) {
        // 1. 채팅방 존재 여부 확인
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("존재하지 않는 채팅방입니다. ID: " + roomId));

        // 2. 해당 채팅방의 모든 메시지 조회
        return chatMessageRepository.findByChatRoom(chatRoom)
                .stream()
                .map(message -> new ChatMessageDto(
                        chatRoom.getId(),
                        message.getSender().getUsername(),
                        message.getContent(),
                        message.getSentAt())
                )
                .collect(Collectors.toList());
    }

    /**
     * 사용자가 참여한 모든 채팅방 목록 조회
     */
    @Transactional(readOnly = true)
    public List<ChatRoomDto> getChatRoomsForUser(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("잘못된 사용자 ID입니다.");
        }

        List<ChatRoom> chatRooms = chatRoomRepository.findAllByUser1IdOrUser2Id(userId, userId);

        if (chatRooms.isEmpty()) {
            throw new ChatRoomNotFoundException("사용자가 참여한 채팅방이 없습니다.");
        }

        // ChatRoom -> ChatRoomDto 변환
        return chatRooms.stream()
                .map(ChatRoomDto::new)
                .collect(Collectors.toList());
    }


}
