package SenierProject.BlockDeal.chat;

import SenierProject.BlockDeal.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_chat_rooms")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member1_id")
    private Member member1;  // 첫 번째 사용자

    @ManyToOne
    @JoinColumn(name = "member2_id")
    private Member member2;  // 두 번째 사용자

    public boolean containsMembers(Member memberA, Member memberB) {
        return (member1.equals(memberA) && member2.equals(memberB)) || (member1.equals(memberB) && member2.equals(memberA));
    }
}