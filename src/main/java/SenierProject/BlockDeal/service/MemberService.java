package SenierProject.BlockDeal.service;

import SenierProject.BlockDeal.dto.RequestJoinDto;
import SenierProject.BlockDeal.dto.RequestLoginDto;
import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.exception.LoginFailedException;
import SenierProject.BlockDeal.jwt.JWTUtil;
import SenierProject.BlockDeal.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final FileUploadService fileUploadService;
    private final JWTUtil jwtUtil; // JWTUtil 추가

    public boolean checkLoginIdDuplicate(String username){
        return memberJpaRepository.existsByUsername(username);
    }

    // BCryptPasswordEncoder 를 통해서 비밀번호 암호화 작업 추가한 회원가입 로직
    public void securityJoin(RequestJoinDto joinRequest){
        if(memberJpaRepository.existsByUsername(joinRequest.getUsername())){
            throw new IllegalArgumentException("이미 존재하는 사용자입니다."); // 예외 던지기
        }

        joinRequest.setPassword(bCryptPasswordEncoder.encode(joinRequest.getPassword()));

        memberJpaRepository.save(joinRequest.toEntity());
    }

    // 로그인 로직: 로그인 실패 시 예외 던지기
    public String login(RequestLoginDto loginRequest) {
        // 사용자 정보 조회
        Member findMember = memberJpaRepository.findByUsername(loginRequest.getUsername());

        // 사용자 정보가 없거나, 비밀번호가 일치하지 않는 경우 예외 처리
        if (findMember == null || !bCryptPasswordEncoder.matches(loginRequest.getPassword(), findMember.getPassword())) {
            throw new LoginFailedException("ID 또는 비밀번호가 일치하지 않습니다!");
        }

        // 인증 성공 시 JWT 토큰 반환
        return jwtUtil.createJwt(findMember.getId(), findMember.getUsername(), String.valueOf(findMember.getRole()), findMember.getName(),
                findMember.getNickname(), findMember.getEmail());
    }

    // 프로필 이미지 업데이트
    @Transactional
    public void updateProfileImage(String username, MultipartFile imageFile) throws IOException {
        Member member = Optional.ofNullable(memberJpaRepository.findByUsername(username))
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        if (!imageFile.isEmpty()) {
            String fileName = fileUploadService.saveFile(imageFile);  // 파일 업로드 서비스 호출
            member.setProfileImageUrl(fileName);  // 프로필 이미지 URL 설정
        }

        memberJpaRepository.save(member);  // 사용자 프로필 업데이트
    }

    // 기존 MemberService의 getUserByUsername 메서드 수정
    public Member getUserByUsername(String username) {
        Member member = memberJpaRepository.findByUsername(username);
        if (member == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return member;
    }

    public Member findById(Long id) {
        return memberJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다. ID: " + id));
    }
}
