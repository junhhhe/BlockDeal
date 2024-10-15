package SenierProject.BlockDeal.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour
    private static final long CLOCK_SKEW = 1000 * 60 * 5; // 5 minutes
    private final SecretKey secretKey;
    private final UserDetailsService userDetailsService;

    // Constructor: HS256 알고리즘 기반 SecretKey 생성
    public JWTUtil(@Value("${spring.jwt.secret}") String secret, UserDetailsService userDetailsService) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.userDetailsService = userDetailsService;
    }

    // 모든 클레임 추출 메서드
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .setAllowedClockSkewSeconds(CLOCK_SKEW / 1000)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("JWT 파싱 오류: " + e.getMessage());
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }

    // 토큰에서 사용자 이름 추출
    public String getUsername(String token) {
        return extractAllClaims(token).get("username", String.class);
    }

    // 토큰에서 역할(Role) 추출
    public String getRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // 토큰이 만료되었는지 확인
    public Boolean isExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date(System.currentTimeMillis()));
    }

    // JWT 토큰에서 인증 정보 생성
    public Authentication getAuthentication(String token) {
        Claims claims = extractAllClaims(token);  // 토큰에서 클레임 추출

        String username = claims.get("username", String.class);
        if (username == null || username.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    // JWT 토큰 생성 메서드
    public String createJwt(Long id, String username, String role, String name, String nickname, String email) {
        return Jwts.builder()
                .claim("id", id)
                .claim("username", username)
                .claim("role", role)
                .claim("name", name)
                .claim("nickname", nickname)
                .claim("email", email)
                .setIssuedAt(new Date(System.currentTimeMillis())) // 생성 시간
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 만료 시간
                .signWith(secretKey)
                .compact();
    }
}
