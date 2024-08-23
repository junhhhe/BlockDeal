package SenierProject.BlockDeal.chat;

import SenierProject.BlockDeal.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 부분 제거

            if (!jwtUtil.isExpired(token)) { // 토큰이 만료되지 않았다면
                String username = jwtUtil.getUsername(token);
                attributes.put("username", username); // 사용자 이름 저장
                System.out.println("username = " + username + " is expired");
                return true; // 인증 성공
            }
        }

        System.out.println("false");
        return false;
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
