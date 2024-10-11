package SenierProject.BlockDeal.config;

import SenierProject.BlockDeal.dto.CustomUserDetails;
import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.jwt.JWTUtil;
import SenierProject.BlockDeal.service.MemberService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JWTUtil jwtUtil;
    private final MemberService memberService;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        log.info("현재 STOMP 명령어: {}", command);

        // 명령어별 처리 로직 추가
        switch (command) {
            case CONNECT:
                handleConnect(accessor);
                break;
            case SUBSCRIBE:
                log.info("사용자 구독 요청 처리");
                break;
            case SEND:
                log.info("메시지 전송 요청 처리");
                // 메시지 전송 시 현재 SecurityContextHolder의 인증 정보를 확인
                log.info("현재 SecurityContextHolder 인증 정보: {}", SecurityContextHolder.getContext().getAuthentication());
                break;
            case DISCONNECT:
                log.info("사용자 연결 해제 처리");
                break;
            default:
                log.warn("알 수 없는 STOMP 명령어: {}", command);
        }

        return message;
    }

    private void handleConnect(StompHeaderAccessor accessor) {
        List<String> authorization = accessor.getNativeHeader("Authorization");
        if (authorization != null && !authorization.isEmpty()) {
            String jwt = authorization.get(0).substring("Bearer ".length());
            try {
                if (!jwtUtil.isExpired(jwt)) {
                    String username = jwtUtil.getUsername(jwt);
                    log.info("JWT 토큰에서 추출한 사용자 이름: {}", username);

                    Member member = memberService.getUserByUsername(username);
                    if (member != null) {
                        CustomUserDetails userDetails = new CustomUserDetails(member);
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.info("사용자 인증 성공 및 SecurityContextHolder에 설정: {}", authentication);
                    } else {
                        log.error("유효하지 않은 사용자입니다: {}", username);
                    }
                } else {
                    log.error("JWT 토큰이 만료되었습니다.");
                }
            } catch (Exception e) {
                log.error("JWT 검증 중 오류 발생: {}", e.getMessage());
            }
        } else {
            log.error("Authorization 헤더가 없습니다. JWT 토큰을 제공해야 합니다.");
        }
    }
}
