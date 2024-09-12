package SenierProject.BlockDeal.chat;

import SenierProject.BlockDeal.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 두 사용자가 정확히 참여하는 채팅방을 조회 (순서 상관없이)
    Optional<ChatRoom> findByMember1AndMember2OrMember2AndMember1(Member member1, Member member2, Member member3, Member member4);

    // 특정 사용자가 참여한 모든 1:1 채팅방 조회
    List<ChatRoom> findAllByMember1OrMember2(Member member1, Member member2);
}