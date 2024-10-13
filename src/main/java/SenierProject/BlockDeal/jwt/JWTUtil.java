package SenierProject.BlockDeal.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
public class JWTUtil {

    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour
    private static final long CLOCK_SKEW = 1000 * 60 * 5; // 5 minutes
    private final SecretKey secretKey;

    // 생성자에서 secretkey 암호화(알고리즘: HS256)
    public JWTUtil(@Value("${spring.jwt.secret}")String secret){

        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    //토큰 내용(Payload) username 인증 메소드
    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    //토큰 내용(payload) role 인증 메소드
    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public String getName(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("name", String.class);
    }

    public String getNickname(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("nickname", String.class);
    }

    public String getEmail(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    //토큰 내용(Payload) 인증 메소드
    // 토큰이 소멸 (유효기간 만료) 하였는지 검증 메서드
    public Boolean isExpired(String token) {

        try{
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date(System.currentTimeMillis() - CLOCK_SKEW));
        } catch(Exception e){
            System.err.println("Error extracting expiration date from token: " + e.getMessage());
            return true;
        }
    }

    // 모든 클레임 추출
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).clockSkewSeconds(CLOCK_SKEW / 1000).build().parseSignedClaims(token).getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("Error parsing JWT token: " + e.getMessage());
            throw new IllegalArgumentException("Invalid token");
        }
    }

    //토근 생성
    public String createJwt(String username, String role, String name, String nickname, String email) {

        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .claim("name", name)
                .claim("nickname", nickname)
                .claim("email", email)
                .issuedAt(new Date(System.currentTimeMillis())) //생성일자
                .expiration(new Date(System.currentTimeMillis()+ EXPIRATION_TIME)) //만료 일자
                .signWith(secretKey)
                .compact();
    }
}
