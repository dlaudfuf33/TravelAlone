package com.example.demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // POST 요청을 통해 새로운 사용자를 등록하는 엔드포인트
    @Operation(summary = "사용자 가입을 위한 엔드포인트")
    @PostMapping("/signup")
    public String signup(@RequestBody UserRequest userRequest) {
        // 사용자 입력값 유효성 검사
        if (userRequest.getUsername() == null || userRequest.getUsername().isEmpty()) {
            return "사용자 이름은 필수 입력 항목입니다.";
        }

        if (userRequest.getPassword() == null || userRequest.getPassword().isEmpty()) {
            return "비밀번호는 필수 입력 항목입니다.";
        }

        if (userRequest.getEmail() == null || userRequest.getEmail().isEmpty()) {
            return "이메일은 필수 입력 항목입니다.";
        }

        // 랜덤 소금 생성
        String salt = SaltGenerator.generateSalt();

        // 입력한 패스워드와 소금을 조합하여 해싱
        String saltedPassword = userRequest.getPassword() + salt;
        String hashedPassword = passwordEncoder.encode(saltedPassword);

        // User 엔티티 생성
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(hashedPassword);
        user.setEmail(userRequest.getEmail());
        user.setSalt(salt); // 생성한 소금 저장

        // UserRepository를 사용하여 사용자 정보를 저장
        userRepository.save(user);

        return "가입 successfully!";
    }


    @GetMapping
    @Operation(summary = "모든 사용자 조회")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @GetMapping("/{userId}")
    @Operation(summary = "특정 ID의 사용자 조회")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
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

//    @CrossOrigin(origins = "http://localhost:3000") // 프론트엔드 출처 허용
//    @GetMapping("/login")
//    public String login() {
//        // ...
//
//        return null;
//    }

}

