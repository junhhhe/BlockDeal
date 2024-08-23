package SenierProject.BlockDeal.service;

import SenierProject.BlockDeal.dto.RequestJoinDto;
import SenierProject.BlockDeal.dto.RequestLoginDto;
import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean checkLoginIdDuplicate(String username){
        return memberJpaRepository.existsByUsername(username);
    }


    // BCryptPasswordEncoder 를 통해서 비밀번호 암호화 작업 추가한 회원가입 로직
    public void securityJoin(RequestJoinDto joinRequest){
        if(memberJpaRepository.existsByUsername(joinRequest.getUsername())){
            return;
        }

        joinRequest.setPassword(bCryptPasswordEncoder.encode(joinRequest.getPassword()));

        memberJpaRepository.save(joinRequest.toEntity());
    }

    public Member login(RequestLoginDto loginRequest) {
        Member findMember = memberJpaRepository.findByUsername(loginRequest.getUsername());

        if (findMember != null && bCryptPasswordEncoder.matches(loginRequest.getPassword(), findMember.getPassword())) {
            return findMember;
        }

        return findMember;
    }

    public Member getLoginMemberById(Long memberId){
        if(memberId == null) return null;

        Optional<Member> findMember = memberJpaRepository.findById(memberId);
        return findMember.orElse(null);

    }
}
