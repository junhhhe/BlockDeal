package SenierProject.BlockDeal.controller;

import SenierProject.BlockDeal.dto.RequestMemberDto;
import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.repository.MemberJpaRepository;
import SenierProject.BlockDeal.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping
    public Member createUser(@RequestBody RequestMemberDto member) {
        return memberService.createMember(member);
    }

   /* @GetMapping("/{username}")
    public Member getUser(@PathVariable String username) {
        Optional<Member> user = memberService.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }*/

    // other methods
}
