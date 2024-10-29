package edu.example.learner.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

@Component
@Log4j2
public class JWTUtil {
    private SecretKey key;

    // application.properties에서 secret 값 가져와서 key에 저장
    public JWTUtil(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // JWT 문자열 생성 (저장 문자열, 만료 시간 - 분 단위)
    public String createToken(Map<String, Object> valueMap, int min) {
        Date now = new Date();  // 토큰 발행 시간

        return Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("type", "JWT")
                .setIssuedAt(now)     // 토큰 발행 시간
                .setExpiration(       // 토큰 만료 시간
                        new Date(now.getTime() +
                                Duration.ofMinutes(min).toMillis()))
                .setClaims(valueMap)  // 저장 데이터
                .signWith(key)        // 서명
                .compact();
    }

    //검증 기능 수행
    public Map<String, Object> validateToken(String token) {
        try {
            Claims claims = Jwts.parser()               //JWT 파싱하고 파서객체 생성
                                .verifyWith(key)        //JWT를 서명하는데 사용된 비밀키 설정
                                .build()                //파서를 설정하여 빌드
                                .parseClaimsJws(token)  //JWT 파싱 및 서명 검증
                                .getBody();             // 클레임 가져오기
            log.info("--- claims : " + claims);
            return claims;
        } catch (IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token");
        }
    }
}
