package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.UserDestination;
import com.example.demo.repository.UserDestinationRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDestinationRepository userDestinationRepository;


    // 사용자 비밀번호를 해시화하여 저장하는 메서드
    public User saveUser(User user) {
        // 사용자 비밀번호를 PasswordEncoder를 사용하여 해시화
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // UserRepository를 사용하여 사용자 정보를 저장하고 반환
        return userRepository.save(user);
    }

    public User registerUser(String userid, String password, String email) {
        User user = new User();
        user.setUserid(userid);
        user.setPassword(password);
        user.setEmail(email);

        return saveUser(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User updateUser(Long userId, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            User foundUser = existingUser.get();
            foundUser.setEmail(updatedUser.getEmail());
            return userRepository.save(foundUser);
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

    /**
     * 사용자가 평가한 여행지와 평점을 반환합니다.
     *
     * @param userId 사용자 ID
     * @return List<UserDestination> 사용자의 여행지 평가 목록
     */
    public List<UserDestination> getUserInteractions(Long userId) {
        return userDestinationRepository.findByUserId(userId);
    }


}
