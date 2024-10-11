package SenierProject.BlockDeal.chat;

import SenierProject.BlockDeal.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // user1과 user2가 같은 채팅방을 조회합니다.
    Optional<ChatRoom> findByUser1AndUser2(Member user1, Member user2);

    // 특정 사용자가 참여한 채팅방을 모두 가져오는 메서드
    List<ChatRoom> findAllByUser1IdOrUser2Id(Long user1Id, Long user2Id);
}