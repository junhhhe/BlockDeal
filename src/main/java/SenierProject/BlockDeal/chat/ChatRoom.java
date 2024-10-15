package SenierProject.BlockDeal.chat;

import SenierProject.BlockDeal.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tbl_chat_room")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roomName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user1_id", nullable = false)  // 외래 키 설정
    @JsonIgnore // 순환 참조 방지를 위해 JSON 직렬화에서 제외
    private Member user1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user2_id", nullable = false)  // 외래 키 설정
    @JsonIgnore // 순환 참조 방지를 위해 JSON 직렬화에서 제외
    private Member user2;

    public ChatRoom(String roomName, Member user1, Member user2) {
        this.roomName = roomName;
        this.user1 = user1;
        this.user2 = user2;
    }
}