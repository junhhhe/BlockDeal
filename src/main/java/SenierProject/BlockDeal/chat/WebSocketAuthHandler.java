package SenierProject.BlockDeal.chat;

import SenierProject.BlockDeal.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketAuthHandler implements HandshakeInterceptor {

    private final JWTUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        System.out.println("WebSocket Handshake initiated");

        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst("Authorization");

        // JWT가 Authorization 헤더에 포함되었는지 확인
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 부분 제거

            try {
                if (!jwtUtil.isExpired(token)) { // 토큰이 만료되지 않았다면
                    String username = jwtUtil.getUsername(token); // 사용자 이름 추출
                    attributes.put("username", username); // WebSocket 세션에 사용자 정보 저장
                    System.out.println("username = " + username + " is authenticated");
                    return true; // 인증 성공
                } else {
                    System.out.println("Token is expired");
                }
            } catch (Exception e) {
                // JWT 관련 예외 처리
                System.out.println("Invalid token: " + e.getMessage());
            }
        } else {
            System.out.println("Authorization header is missing or invalid");
        }

        return false; // 인증 실패 시 WebSocket 연결 거부
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        if (exception != null) {
            System.out.println("WebSocket Handshake failed with exception: " + exception.getMessage());
        } else {
            System.out.println("WebSocket Handshake succeeded for URI: " + request.getURI());
        }
    }
}