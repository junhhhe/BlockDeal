package SenierProject.BlockDeal.controller;

import SenierProject.BlockDeal.dto.ApiResponse;
import SenierProject.BlockDeal.dto.CustomUserDetails;
import SenierProject.BlockDeal.dto.RequestJoinDto;
import SenierProject.BlockDeal.dto.RequestLoginDto;
import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public ResponseEntity<ApiResponse<RequestJoinDto>> joinPage() {
        RequestJoinDto joinRequest = new RequestJoinDto();
        return ResponseEntity.ok(new ApiResponse<>(true, "회원가입 페이지", joinRequest));
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<String>> join(@RequestBody RequestJoinDto joinRequest, BindingResult bindingResult) {
        memberService.securityJoin(joinRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "회원가입 성공", null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody RequestLoginDto loginRequest){
        log.info("로그인 요청 - username: {}", loginRequest.getUsername());
        String token = memberService.login(loginRequest); // 토큰 반환
        log.info("로그인 성공 - username: {}, JWT Token: {}", loginRequest.getUsername(), token);
        return ResponseEntity.ok(new ApiResponse<>(true, "로그인 성공", token));
    }

    @GetMapping("/info")
    @Transactional
    public ResponseEntity<ApiResponse<Member>> memberInfo(Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Member loginMember = userDetails.getMember();

        // 로그로 값 확인
        System.out.println("Member nickname: " + loginMember.getNickname());
        System.out.println("Member email: " + loginMember.getEmail());
        System.out.println("Member name:" + loginMember.getName());
        System.out.println("Member username: " + loginMember.getUsername());


        return ResponseEntity.ok(new ApiResponse<>(true, "회원 정보 조회 성공", loginMember));
    }

    // 특정 username으로 Member ID를 조회하는 API
    @GetMapping("/id")
    public ResponseEntity<Long> getUserId(@RequestParam("username") String username) {
        log.info("Member ID 조회 요청 - username: {}", username);
        Member member = memberService.getUserByUsername(username);
        if (member == null) {
            log.error("회원 정보 조회 실패 - 존재하지 않는 username: {}", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        log.info("회원 정보 조회 성공 - username: {}, memberId: {}", username, member.getId());
        return ResponseEntity.ok(member.getId());
    }

    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<String>> adminPage() {
        return ResponseEntity.ok(new ApiResponse<>(true, "인가 성공!", null));
    }
}
