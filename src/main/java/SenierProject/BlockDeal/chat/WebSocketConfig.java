package SenierProject.BlockDeal.chat;

import SenierProject.BlockDeal.config.JwtChannelInterceptor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtChannelInterceptor jwtChannelInterceptor; // 주입된 JwtChannelInterceptor 사용
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    // 애플리케이션 초기화 시점에 SecurityContextHolder의 모드를 INHERITABLETHREADLOCAL로 설정
    @PostConstruct
    public void init() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub"); // 메시지를 구독(수신)하는 엔드포인트
        config.setApplicationDestinationPrefixes("/pub"); // 메시지를 발행(송신)하는 엔드포인트
        logger.info("MessageBroker 설정 완료");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:3001") // 허용할 도메인 명시
                .withSockJS();
        logger.info("STOMP 엔드포인트 등록 완료: /ws");
    }

    @Override
    public void configureClientInboundChannel(org.springframework.messaging.simp.config.ChannelRegistration registration) {
        logger.info("Inbound Channel 설정 시작");
        registration.interceptors(jwtChannelInterceptor); // JwtChannelInterceptor 추가
        logger.info("Inbound Channel에 JwtChannelInterceptor 추가 완료");
    }
}
