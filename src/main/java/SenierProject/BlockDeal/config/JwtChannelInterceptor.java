package SenierProject.BlockDeal.config;

import SenierProject.BlockDeal.jwt.JWTUtil;
import SenierProject.BlockDeal.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JWTUtil jwtUtil;
    private final MemberService memberService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor
                .getAccessor(message, StompHeaderAccessor.class);

        // 토큰을 핸드셰이크 세션에서 가져오기
        String token = (String) accessor.getSessionAttributes().get("token");
        if (token != null) {
            try {
                // JWT 토큰에서 인증 객체 생성
                Authentication authentication = jwtUtil.getAuthentication(token);

                // SecurityContext에 설정
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);

                // Principal로 설정 (올바른 형변환)
                if (authentication.getPrincipal() instanceof Principal) {
                    accessor.setUser((Principal) authentication.getPrincipal());
                } else {
                    throw new IllegalStateException("인증 객체가 Principal이 아닙니다.");
                }

            } catch (Exception e) {
                throw new IllegalStateException("유효하지 않은 JWT 토큰입니다.", e);
            }
        }
        return message;
    }
}
