package com.tenco.class_jwt_v01.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {
    @Value("${jwt.secret}")
    private String secret;

    // 사용자 로그인 -> username, password --> jwt 발급
    // 액세스 토큰 생성
    public String generateAccessToken(String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withSubject("myApp")
                    .withClaim("username", username)
                    .withClaim("role", "USER")
                    .withIssuedAt(new Date()) // 페이로드에 발행시간 설정
                    .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                    .sign(algorithm);

        } catch (JWTCreationException e) {
            throw new RuntimeException("액세스 토큰 생성 실패", e);
        }
    }

    // 리프레시 토큰 생성 7일
    public String generateRefreshToken(String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withSubject("myApp")
                    .withClaim("username", username)
                    .withClaim("role", "USER")
                    .withIssuedAt(new Date()) // 페이로드에 발행시간 설정
                    .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 360 * 24 * 7))
                    .sign(algorithm);

        } catch (JWTCreationException e) {
            throw new RuntimeException("리프레쉬 토큰 생성 실패", e);
        }
    }

    // JWT 토큰에서 username만 압축 풀어서 꺼내는 기능
    public String extractUsername(String token){
        return JWT.decode(token)
                .getClaim("username")
                .asString();
    }

    // role 꺼내기
    public String extractRole(String token){
        return JWT.decode(token)
                .getClaim("role")
                .asString();
    }

    // 토큰 유효성 검사
    // 토큰 시간이 유효한지, 위변조되지 않았는지 확인
    public boolean validateToken(String token, String username){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWT.require(algorithm) // 이 알고리즘으로 서명된 토큰만 허용
                    .build()
                    .verify(token); // 토큰 검증(만료시간, 위조 체크)
            String extractedUsername = extractUsername(token);
            return extractedUsername != null && extractedUsername.equals(username);
        }catch (Exception e){
            return false;
        }
    }
}
