package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserService userService;


    public User registerUser(String username, String password, String email) {
        // 사용자 객체 생성
        User user = new User();
        user.setUserid(username);
        user.setPassword(password); // 비밀번호는 암호화된 상태로 저장됩니다.
        user.setEmail(email);

        // UserService를 사용하여 사용자 저장
        return userService.saveUser(user);
    }
}
