package com.example.demo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;

@Controller
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // JWT 비밀 키
    @Value("${jwt.secret}")
    private String jwtSecret;

    // JWT 만료 시간 (초 단위)
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        // 사용자 정보를 가져옵니다.
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 입력된 비밀번호와 저장된 해시된 비밀번호를 비교하여 인증을 수행합니다.
        if (userDetails != null && passwordEncoder.matches(password, userDetails.getPassword())) {
            // Spring Security의 AuthenticationManager를 사용하여 사용자를 인증합니다.
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // 인증된 사용자를 SecurityContext에 설정합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 여기서 인증 토큰을 생성하고 반환합니다.
            String token = generateAuthToken(userDetails); // 인증 토큰 생성 메서드

            // 인증 토큰을 JSON 응답에 포함시켜 반환합니다.
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } else {
            // 인증 실패 시 에러 응답을 반환합니다.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Invalid credentials"));
        }
    }

    // 인증 토큰을 생성하는 메서드
    private String generateAuthToken(UserDetails userDetails) {
        // 현재 시간을 기반으로 Date 객체를 생성합니다. 이는 토큰 발행 시간으로 사용됩니다.
        Date now = new Date();

        // 토큰의 만료 시간을 설정합니다. 현재 시간에서 설정된 만료 시간(초 단위)을 더한 값을 가진 Date 객체를 생성합니다.
        Date expiryDate = new Date(now.getTime() + jwtExpiration * 1000);

        // JWT 빌더를 사용하여 토큰을 생성합니다.
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // 토큰의 주체로 사용자명을 설정합니다. 이는 토큰을 해석했을 때 어느 사용자의 토큰인지 확인하는데 사용됩니다.
                .setIssuedAt(now) // 토큰 발행 시간을 설정합니다.
                .setExpiration(expiryDate) // 토큰의 만료 시간을 설정합니다.
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // HMAC-SHA512 알고리즘과 비밀 키를 사용하여 토큰에 서명합니다.
                .compact(); // JWT를 압축된 문자열 형태로 반환합니다.
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout"; // 로그아웃 후 리다이렉트할 페이지
    }
}

