package com.example.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    // JWT 비밀 키
    @Value("${jwt.secret}")
    private String jwtSecret;

    // JWT 만료 시간 (초 단위)
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // 인증 토큰을 생성하는 메서드
    public String generateAuthToken(String userid) {
        // 현재 시간을 기반으로 Date 객체를 생성합니다. 이는 토큰 발행 시간으로 사용됩니다.
        Date now = new Date();
        // 토큰의 만료 시간을 설정합니다. 현재 시간에서 설정된 만료 시간(초 단위)을
        // 더한 값을 가진 Date 객체를 생성합니다.
        Date expiryDate = new Date(now.getTime() + jwtExpiration * 1000);

        return Jwts.builder()
                .setSubject(userid)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Claims parseToken(String jwtToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(jwtToken)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }
}
