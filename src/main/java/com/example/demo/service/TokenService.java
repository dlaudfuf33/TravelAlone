package com.example.demo.service;

import com.example.demo.entity.BlacklistedToken;
import com.example.demo.repository.BlacklistedTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    // JWT 비밀 키
    @Value("${jwt.secret}")
    private String jwtSecret;

    // JWT 만료 시간 (초 단위)
    @Value("${jwt.expiration}")
    private long jwtExpiration;


    private final BlacklistedTokenRepository blacklistedTokenRepository;



    // 인증 토큰을 생성하는 메서드
    public String generateAuthToken(String userid) {
        Date now = new Date();
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
