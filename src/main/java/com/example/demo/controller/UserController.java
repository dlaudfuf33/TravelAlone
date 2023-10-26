package com.example.demo.controller;

import com.example.demo.*;
import com.example.demo.entity.BlacklistedToken;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRequest;
import com.example.demo.repository.BlacklistedTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TokenService;
import com.example.demo.service.UserService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private TokenService tokenService;

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;


    @GetMapping("/userinfo")
    @Operation(summary = "사용자 정보를 가져오는 엔드포인트")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
        try {

            // "Bearer 토큰값" 형태에서 실제 토큰 값을 추출합니다.
            String jwtToken = token.substring(7); // "Bearer " 부분을 제거

            // 토큰이 블랙리스트에 있는지 확인합니다.
            if (tokenService.isTokenBlacklisted(jwtToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This token has been blacklisted.");
            }
            // 토큰을 검증하고 클레임(Claims) 객체를 얻습니다.

            Claims claims = tokenService.parseToken(jwtToken);

            // 클레임에서 필요한 정보를 추출합니다.
            String userid = (String) claims.get("sub");

            User user = userRepository.findByUserid(userid);
            if (user != null) {
                // 필요한 사용자 정보를 응답에 추가
                Map<String, Object> response = new HashMap<>();
                response.put("id", user.getId());
                response.put("userid", user.getUserid());
//                response.put("userPassword",user.getPassword());
                response.put("userEmail", user.getEmail());
                response.put("usergender", user.getGender());
                response.put("userage", user.getAge());
                response.put("city", user.getCity());
                response.put("district", user.getDistrict());
                response.put("detailedAddress", user.getDetailedAddress());
                response.put("registrationDate", user.getRegistrationDate());
                response.put("dateOfBirth", user.getDateOfBirth());

                return ResponseEntity.ok(response);
            } else {
                // 사용자를 찾을 수 없는 경우에 대한 처리
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            // 토큰이 유효하지 않을 경우 예외 처리
            System.out.println(e+"토큰이 유효하지 않을 경우 예외 처리");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

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
        parseAndSetAddress(user,userRequest.getAddress());


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

    @PutMapping("/update/{userId}")
    @Operation(summary = "특정 ID의 사용자 업데이트")
    public ResponseEntity<?> updateUser(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String token,
            @RequestBody UserRequest updatedUserInfo) {
        try {
            // 토큰에서 사용자 ID 추출
            String jwtToken = token.substring(7); // "Bearer " 제거
            if (tokenService.isTokenBlacklisted(jwtToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 블랙리스트에 있습니다.");
            }
            Claims claims = tokenService.parseToken(jwtToken);
            String userid = (String) claims.get("sub");

            // 요청받은 ID와 토큰의 ID가 일치하는지 검증
            if (!userId.equals(userRepository.findByUserid(userid).getId())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 시도입니다.");
            }

            // DB에서 사용자 정보 가져오기
            User user = userRepository.findByUserid(userid);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
            }

            // 요청된 비밀번호가 현재 비밀번호와 일치하는지 확인
            String providedPassword = updatedUserInfo.getPassword(); // 사용자가 요청 본문에 제공한 비밀번호
            String salt = user.getSalt(); // DB에서 가져온 솔트
            String saltedPassword = providedPassword + salt; // 솔트 추가

            if (!passwordEncoder.matches(saltedPassword, user.getPassword())) {
                // 비밀번호가 일치하지 않는 경우
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 틀렸습니다.");
            }

            // 주소 파싱 및 설정
            parseAndSetAddress(user, updatedUserInfo.getAddress());

            // 기타 사용자 정보 업데이트
            user.setEmail(updatedUserInfo.getEmail());
            user.setGender(updatedUserInfo.getGender());
            user.setDateOfBirth(updatedUserInfo.getDateOfBirth());
            // 필요하다면 user 객체에 대한 추가적인 setter 메서드 호출을 여기에 더 추가할 수 있습니다.

            User updatedUser = userService.updateUser(userId, user); // DB 업데이트

            return ResponseEntity.ok(updatedUser); // 업데이트된 사용자 정보 반환

        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류가 발생했습니다: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{userId}")
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
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> requestBody) {
        String userid = requestBody.get("userid");
        String password = requestBody.get("password");
        System.out.println("userid : " + userid +"//"+ "password : "+ password);
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
                // JWT 토큰을 생성합니다.
                String token = tokenService.generateAuthToken(userid); // TokenService를 사용하여 토큰 생성
                // 추가적인 회원 정보를 JSON 응답에 포함시켜 반환합니다.
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("userid", userid);
                response.put("usergender", user.getGender());
                response.put("userage", user.getAge());
                // 토큰을 JSON 응답에 포함시켜 반환합니다.
                System.out.println("Login-");
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 로그인 실패
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        // 토큰의 유효기간을 설정합니다. (예: 토큰의 유효기간)
        blacklistedToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        blacklistedTokenRepository.save(blacklistedToken);

        return ResponseEntity.ok().build();
    }

    // 주소 파싱 메서드
    private void parseAndSetAddress(User user, String address) {
        String[] addressParts = address.split(" ");
        if (addressParts.length >= 4) {
            String city = addressParts[0]; // 시/도
            String district = addressParts[1] + " " + addressParts[2]; // 군/구
            StringBuilder detailedAddress = new StringBuilder();
            for (int i = 3; i < addressParts.length; i++) {
                detailedAddress.append(addressParts[i]).append(" ");
            }

            user.setCity(city);
            user.setDistrict(district);
            user.setDetailedAddress(detailedAddress.toString().trim());

        } else {
            System.out.println("주소 형식이 올바르지 않습니다.");
        }
    }
}

