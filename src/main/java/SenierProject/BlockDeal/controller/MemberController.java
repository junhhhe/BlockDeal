package SenierProject.BlockDeal.controller;

import SenierProject.BlockDeal.dto.ApiResponse;
import SenierProject.BlockDeal.dto.CustomUserDetails;
import SenierProject.BlockDeal.dto.RequestJoinDto;
import SenierProject.BlockDeal.dto.RequestLoginDto;
import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.jwt.JWTUtil;
import SenierProject.BlockDeal.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Iterator;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MemberController {

    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    @GetMapping("/")
    public ResponseEntity<ApiResponse<String>> home() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        String responseMessage = "MemberController: " + username + ", " + role;

        return ResponseEntity.ok(new ApiResponse<>(true, "성공", responseMessage));
    }

    @GetMapping("/join")
    public ResponseEntity<ApiResponse<RequestJoinDto>> joinPage() {

        RequestJoinDto joinRequest = new RequestJoinDto();

        return ResponseEntity.ok(new ApiResponse<>(true, "회원가입 페이지", joinRequest));
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<String>> join(@RequestBody RequestJoinDto joinRequest, BindingResult bindingResult) {

        if (memberService.checkLoginIdDuplicate(joinRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(false, "ID가 존재합니다.", null));
        }

        if (!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "비밀번호가 일치하지 않습니다.", null));
        }

        // 이메일 검증 추가
        if (joinRequest.getEmail() == null || joinRequest.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "이메일을 입력해주세요.", null));
        }

        memberService.securityJoin(joinRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "회원가입 성공", null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody RequestLoginDto loginRequest){

        Member member = memberService.login(loginRequest);

        if(member == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "ID 또는 비밀번호가 일치하지 않습니다!", null));
        }

        String token = jwtUtil.createJwt(member.getUsername(), String.valueOf(member.getRole()), member.getName(), member.getNickname(), member.getEmail());

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

    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<String>> adminPage() {
        return ResponseEntity.ok(new ApiResponse<>(true, "인가 성공!", null));
    }
}
