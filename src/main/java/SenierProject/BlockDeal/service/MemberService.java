package SenierProject.BlockDeal.service;

import SenierProject.BlockDeal.dto.RequestMemberDto;
import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    /**
     * 새로운 사용자를 생성하고, 생성된 사용자를 반환.
     * param : user 새로 생성할 사용자 정보
     * return : 생성된 사용자 정보
     */
    public Member createMember(RequestMemberDto dto) {

        Member member = Member.builder().username(dto.getUsername()).password(dto.getPassword()).build();
        return memberJpaRepository.save(member);
    }

    /**
     * 주어진 사용자명(username)에 해당하는 사용자 정보를 조회.
     * param : username 조회할 사용자명
     * return 사용자 정보가 존재하는 경우 해당 정보를, 그렇지 않은 경우 null을 반환.
     */
    /*public Optional<Member> findByUsername(String username) {
        return memberJpaRepository.findByName(username);
    }*/
}
