package SenierProject.BlockDeal.chat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    /**
     * 특정 채팅방에 속한 모든 메시지 조회
     */
    List<ChatMessage> findByChatRoom(ChatRoom chatRoom);
}