package SenierProject.BlockDeal.service;

import SenierProject.BlockDeal.dto.RequestMemberDto;
import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.repository.MemberJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JoinService {

    private final MemberJpaRepository memberJpaRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //회원가입 로직
    public void joinProcess(RequestMemberDto requestMemberDto){

        String username = requestMemberDto.getUsername();
        String password = requestMemberDto.getPassword();

        Boolean isExist = memberJpaRepository.existsByUsername(username);

        if(isExist){

            return;
        }

        Member data = new Member();
        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setRole("ROLE_ADMIN");

        memberJpaRepository.save(data);
    }
}
