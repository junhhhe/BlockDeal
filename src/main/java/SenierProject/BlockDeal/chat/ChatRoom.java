package SenierProject.BlockDeal.chat;

import SenierProject.BlockDeal.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "tbl_chat_room")
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {


    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // UUID 사용

    // 최근 메시지 추적
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "last_chat_message_id")
    private ChatMessage lastChatMessage;

    // 두 명의 사용자 (ManyToOne 관계로 간단하게)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member1_id", nullable = false)
    private Member member1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member2_id", nullable = false)
    private Member member2;

    // 채팅방에 속한 메시지들 (양방향 관계 설정)
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // UUID 생성
    public static ChatRoom create(Member member1, Member member2) {
        ChatRoom room = new ChatRoom();
        room.setMember1(member1);
        room.setMember2(member2);
        return room;
    }

    public void addLastMessage(ChatMessage message) {
        this.lastChatMessage = message;
    }
}
