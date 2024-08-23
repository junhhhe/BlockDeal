package SenierProject.BlockDeal.chat;

import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberJpaRepository memberRepository;

    @PreAuthorize("isAuthenticated()")  // 인증된 사용자만 접근 가능
    public ChatRoom createOrGetChatRoom(String username1, String username2) {
        // username으로 Member 객체 조회
        Member member1 = memberRepository.findByUsername(username1);
        Member member2 = memberRepository.findByUsername(username2);

        if (member1 == null || member2 == null) {
            throw new IllegalArgumentException("Invalid username provided.");
        }

        // 기존에 두 사용자가 참여하고 있는 채팅방이 있는지 확인
        Optional<ChatRoom> existingChatRoom = chatRoomRepository.findByMembers(member1, member2);

        if (existingChatRoom.isPresent()) {
            return existingChatRoom.get();  // 이미 존재하는 채팅방을 반환
        }

        // 기존 채팅방이 없다면 새로 생성
        ChatRoom newChatRoom = ChatRoom.builder()
                .member1(member1)
                .member2(member2)
                .build();

        return chatRoomRepository.save(newChatRoom);
    }

    // 메시지 전송 및 저장
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public ChatMessage sendMessage(String senderUsername, Long chatRoomId, String content) {
        // Sender 조회
        Member sender = memberRepository.findByUsername(senderUsername);
        if (sender == null) {
            throw new IllegalArgumentException("Invalid username provided.");
        }

        // ChatRoom 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat room ID: " + chatRoomId));

        // 메시지 생성 및 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .sender(sender.getUsername())  // sender의 username을 저장
                .content(content)
                .timestamp(LocalDateTime.now())
                .chatRoom(chatRoom)
                .build();

        return chatMessageRepository.save(chatMessage);
    }

    // 모든 채팅방 조회
    @PreAuthorize("isAuthenticated()")  // 인증된 사용자만 접근 가능
    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    @PreAuthorize("isAuthenticated()")  // 인증된 사용자만 접근 가능
    public List<ChatMessage> getMessagesByRoomId(Long roomId) {
        return chatMessageRepository.findByChatRoomId(roomId);
    }

    @PreAuthorize("isAuthenticated()")  // 인증된 사용자만 접근 가능
    public ChatRoom findChatRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID: " + roomId));
    }
}