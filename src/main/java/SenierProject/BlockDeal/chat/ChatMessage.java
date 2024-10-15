package SenierProject.BlockDeal.chat;

import SenierProject.BlockDeal.chat.chatdto.MessageType;
import SenierProject.BlockDeal.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    @JsonIgnore  // ChatRoom 필드의 직렬화를 방지
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;  // 메시지를 보낸 사용자

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageType type;

    // 모든 필드를 포함한 생성자
    public ChatMessage(ChatRoom chatRoom, Member sender, String content, MessageType type, LocalDateTime sentAt) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.content = content;
        this.type = type;
        this.sentAt = sentAt;
    }

    public void setUser(Member member) {
        this.sender = member;
    }
}