package com.example.demo.controller;

import com.example.demo.*;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@Tag(name = "User API", description = "사용자(User) 관련 API 엔드포인트")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    // JWT 비밀 키
    @Value("${jwt.secret}")
    private String jwtSecret;

    // JWT 만료 시간 (초 단위)
    @Value("${jwt.expiration}")
    private long jwtExpiration;


    // POST 요청을 통해 새로운 사용자를 등록하는 엔드포인트
    @Operation(summary = "사용자 가입을 위한 엔드포인트")
    @PostMapping("/signup")
    public String signup(@RequestBody UserRequest userRequest) {
        System.out.println(userRequest);
        LocalDateTime currentTime = LocalDateTime.now();
        // 사용자 입력값 유효성 검사
        if (userRequest.getUserid() == null || userRequest.getUserid().isEmpty()) {
            return "사용자 계정명은 필수 입력 항목입니다.";
        }

        if (userRequest.getPassword() == null || userRequest.getPassword().isEmpty()) {
            return "비밀번호는 필수 입력 항목입니다.";
        }

        if (userRequest.getEmail() == null || userRequest.getEmail().isEmpty()) {
            return "이메일은 필수 입력 항목입니다.";
        }
        User user = new User();
        // 주소 정보 파싱 및 분리
        String[] addressParts = userRequest.getAddress().split(" ");
        if (addressParts.length >= 4) {
            String city = addressParts[0]; // 시/도
            String district = addressParts[1] + " " + addressParts[2]; // 군/구
            StringBuilder detailedAddress = new StringBuilder();
            for (int i = 3; i < addressParts.length; i++) {
                detailedAddress.append(addressParts[i]).append(" ");
            }

            // 결과 출력
            System.out.println("시/도: " + city);
            System.out.println("군/구: " + district);
            System.out.println("상세주소: " + detailedAddress.toString().trim());
            user.setCity(city);
            user.setDistrict(district);
            user.setDetailedAddress(detailedAddress.toString().trim());

        } else {
            System.out.println("주소 형식이 올바르지 않습니다.");
        }

        // 랜덤 소금 생성
        String salt = SaltGenerator.generateSalt();

        // 입력한 패스워드와 소금을 조합하여 해싱
        String saltedPassword = userRequest.getPassword() + salt;
        String hashedPassword = passwordEncoder.encode(saltedPassword);

        // User 엔티티 생성
        user.setUserid(userRequest.getUserid());
        user.setPassword(hashedPassword);
        user.setEmail(userRequest.getEmail());
        user.setSalt(salt); // 생성한 소금 저장
        user.setDateOfBirth(userRequest.getDateOfBirth());
        user.setGender(userRequest.getGender());
        user.setRegistrationDate(currentTime);


        // UserRepository를 사용하여 사용자 정보를 저장
        userRepository.save(user);

        return "가입 successfully!";
    }

    @PutMapping("/{userId}")
    @Operation(summary = "특정 ID의 사용자 업데이트")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
        User user = userService.updateUser(userId, updatedUser);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{userId}")
    @Operation(summary = "특정 ID의 사용자 삭제")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        boolean deleted = userService.deleteUser(userId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    @Operation(summary = "사용자 로그인을 위한 엔드포인트")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> requestBody, HttpSession session)
    {
        String userid = requestBody.get("userid");
        String password = requestBody.get("password");

        System.out.println("userid : " + userid + "password : "+ password);
        // 사용자 정보를 가져옵니다.
        User user = userRepository.findByUserid(userid);
        System.out.println("객체 : " + user);

        if (user != null) {
            // 사용자가 존재하면 비밀번호를 비교하여 인증을 수행합니다.
            String storedPassword = user.getPassword();
            String salt = user.getSalt();
            String saltedPassword = password + salt;

            if (passwordEncoder.matches(saltedPassword, storedPassword)) {
                // 비밀번호가 일치하면 로그인 성공 처리를 합니다.

                // 사용자 정보를 세션에 저장합니다.
                session.setAttribute("USER", user);

                // 추가적인 회원 정보를 JSON 응답에 포함시켜 반환합니다.
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("userid", userid);
                responseMap.put("usergender", user.getGender());
                responseMap.put("userage", user.getAge());
                return ResponseEntity.ok(responseMap);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 로그인 실패
    }

    @PostMapping("/logout")
    @Operation(summary = "사용자 로그아웃을 위한 엔드포인트")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    // 인증 토큰을 생성하는 메서드
    private String generateAuthToken(String userid) {
        // 현재 시간을 기반으로 Date 객체를 생성합니다. 이는 토큰 발행 시간으로 사용됩니다.
        Date now = new Date();

        // 토큰의 만료 시간을 설정합니다. 현재 시간에서 설정된 만료 시간(초 단위)을 더한 값을 가진 Date 객체를 생성합니다.
        Date expiryDate = new Date(now.getTime() + jwtExpiration * 1000);

        // JWT 빌더를 사용하여 토큰을 생성합니다.
        return Jwts.builder()
                .setSubject(userid) // 토큰의 주체로 사용자명을 설정합니다. 이는 토큰을 해석했을 때 어느 사용자의 토큰인지 확인하는데 사용됩니다.
                .setIssuedAt(now) // 토큰 발행 시간을 설정합니다.
                .setExpiration(expiryDate) // 토큰의 만료 시간을 설정합니다.
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // HMAC-SHA512 알고리즘과 비밀 키를 사용하여 토큰에 서명합니다.
                .compact(); // JWT를 압축된 문자열 형태로 반환합니다.
    }

}

