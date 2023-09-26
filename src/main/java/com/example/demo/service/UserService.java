package com.example.demo;import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;


    // 사용자 비밀번호를 해시화하여 저장하는 메서드
    public User saveUser(User user) {
        // 사용자 비밀번호를 PasswordEncoder를 사용하여 해시화
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // UserRepository를 사용하여 사용자 정보를 저장하고 반환
        return userRepository.save(user);
    }

    public User registerUser(String username, String password, String email, String roleName) {
        // 사용자 객체 생성
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        // 권한 객체 생성 또는 가져오기
        Role role = roleService.getRoleByName(roleName);
        if (role == null) {
            role = roleService.createRole(roleName);
        }

        // 사용자에게 권한 부여
        user.getRoles().add(role);

        // 사용자 저장
        return saveUser(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User updateUser(Long userId, User updatedUser) {
        User existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser != null) {
            // 업데이트 로직을 구현하고 저장
            // 예를 들어, updatedUser의 필드들을 existingUser에 복사하고 저장
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            // ...
            return userRepository.save(existingUser);
        } else {
            return null;
        }
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        } else {
            return false;
        }
    }
}
