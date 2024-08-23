package SenierProject.BlockDeal.chat;

import SenierProject.BlockDeal.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>{

    Optional<ChatRoom> findByMember1AndMember2(Member member1, Member member2);

    // 또는 두 멤버가 서로 바뀌는 경우를 모두 고려하여 조회
    default Optional<ChatRoom> findByMembers(Member member1, Member member2) {
        return findByMember1AndMember2(member1, member2)
                .or(() -> findByMember1AndMember2(member2, member1));
    }
}