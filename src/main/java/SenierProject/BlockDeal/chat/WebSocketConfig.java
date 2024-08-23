package SenierProject.BlockDeal.chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketAuthHandler webSocketAuthHandler;

    public WebSocketConfig(WebSocketAuthHandler webSocketAuthHandler) {
        this.webSocketAuthHandler = webSocketAuthHandler;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub");//구독 경로
        config.setApplicationDestinationPrefixes("/pub");//메세지 전송 경로
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*") //허용할 도메인 명시
                .addInterceptors(webSocketAuthHandler)
                .withSockJS();
    }

}