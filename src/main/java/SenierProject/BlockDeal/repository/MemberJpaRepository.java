package SenierProject.BlockDeal.repository;

import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.jwt.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    // 로그인 ID를 갖는 객체가 존재하는지 => 존재하면 true 리턴 (ID 중복 검사 시 필요)
    Boolean existsByUsername(String username);
    Member findByUsername(String username);
    Optional<Member> findByIdAndRole(Long id, MemberRole role);

}
