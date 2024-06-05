package SenierProject.BlockDeal.repository;

import SenierProject.BlockDeal.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    //Optional<Member> findByName(String name);
    Boolean existsByUsername(String username);

    Member findByUsername(String username);

}
