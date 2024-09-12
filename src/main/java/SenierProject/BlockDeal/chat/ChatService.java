package SenierProject.BlockDeal.chat;

import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberJpaRepository memberRepository;

    // 1. 채팅방 생성 또는 기존 채팅방 조회
    @Transactional
    @PreAuthorize("isAuthenticated()")  // 인증된 사용자만 접근 가능
    public ChatRoom createOrGetChatRoom(String username1, String username2) {
        // 사용자 1 조회 및 null 체크
        Member member1 = Optional.ofNullable(memberRepository.findByUsername(username1))
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username1));
        Member member2 = Optional.ofNullable(memberRepository.findByUsername(username2))
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username2));

        // 기존에 두 사용자가 참여하고 있는 채팅방이 있는지 확인
        Optional<ChatRoom> existingChatRoom = chatRoomRepository.findByMember1AndMember2OrMember2AndMember1(member1, member2, member1, member2);

        if (existingChatRoom.isPresent()) {
            return existingChatRoom.get();  // 이미 존재하는 채팅방을 반환
        }

        // 채팅방이 없으면 새로운 채팅방을 생성
        ChatRoom newChatRoom = ChatRoom.builder()
                .member1(member1)
                .member2(member2)
                .build();

        return chatRoomRepository.save(newChatRoom);
    }

    // 2. 메시지 전송 및 저장
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public ChatMessage sendMessage(String senderUsername, Long chatRoomId, String messageContent) {
        // 메시지 작성자를 조회
        Member sender = Optional.ofNullable(memberRepository.findByUsername(senderUsername))
                .orElseThrow(() -> new IllegalArgumentException("Invalid username: " + senderUsername));

        // 채팅방을 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat room ID: " + chatRoomId));

        // 메시지 생성 및 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .author(sender)
                .chatRoom(chatRoom)
                .message(messageContent)
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        // 최근 메시지 업데이트
        chatRoom.addLastMessage(savedMessage);
        chatRoomRepository.save(chatRoom);

        return savedMessage;
    }

    // 3. 특정 채팅방의 모든 메시지 조회
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public List<ChatMessage> getMessagesByChatRoom(Long chatRoomId) {
        return chatMessageRepository.findByChatRoomId(chatRoomId);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public List<ChatRoom> getAllChatRoomsForUser(String username) {
        // 사용자 조회
        Member member = Optional.ofNullable(memberRepository.findByUsername(username))
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        // 사용자가 속한 모든 채팅방 조회 (member1 또는 member2로 속한 채팅방)
        return chatRoomRepository.findAllByMember1OrMember2(member, member);
    }

    // 5. 채팅방 조회
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public ChatRoom getChatRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("ChatPage room not found: " + roomId));
    }
}
