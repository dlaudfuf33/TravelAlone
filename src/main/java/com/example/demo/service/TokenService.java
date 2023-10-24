package com.example.demo.service;

import com.example.demo.entity.BlacklistedToken;
import com.example.demo.repository.BlacklistedTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class TokenService {

    // JWT 비밀 키
    @Value("${jwt.secret}")
    private String jwtSecret;

    // JWT 만료 시간 (초 단위)
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;



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

    @Scheduled(fixedRate = 3600000) // 매 시간마다 실행
    public void removeExpiredTokens() {
        List<BlacklistedToken> expiredTokens =
                blacklistedTokenRepository.findAllByExpiryDateBefore(LocalDateTime.now());
        blacklistedTokenRepository.deleteAll(expiredTokens);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepository.findByToken(token).isPresent();
    }
}
