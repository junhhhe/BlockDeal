package SenierProject.BlockDeal.chat;

import SenierProject.BlockDeal.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tbl_chat_message")
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(value = {AuditingEntityListener.class})
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ChatRoom과의 관계 설정 (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    // 메시지 작성자와의 관계 설정 (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    // 메시지 내용
    @Column(name = "message", nullable = false)
    private String message;

    // 메시지 생성 시간
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
