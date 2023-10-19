package com.example.demo.service;

import com.example.demo.entity.Destination;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserDestination;
import com.example.demo.repository.DestinationRepository;
import com.example.demo.repository.UserDestinationRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;


import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserDestinationRepository userDestinationRepository;

    @Autowired
    private DestinationRepository destinationRepository;


    // 사용자 비밀번호를 해시화하여 저장하는 메서드
    public User saveUser(User user) {
        // 사용자 비밀번호를 PasswordEncoder를 사용하여 해시화
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // UserRepository를 사용하여 사용자 정보를 저장하고 반환
        return userRepository.save(user);
    }

    public User registerUser(String userid, String password, String email, String roleName) {
        // 사용자 객체 생성
        User user = new User();
        user.setUserid(userid);
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
            existingUser.setUserid(updatedUser.getUserid());
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


    /**
     * 인기 있는 여행지를 추천합니다.
     *
     * @param topN 추천 받고 싶은 여행지의 수
     * @return List<Destination> 추천 여행지 목록
     */
    public List<Destination> recommendPopularDestinations(int topN) {
        // 평균 평점이 높은 여행지를 순서대로 추출
        Pageable topNPageable = PageRequest.of(0, topN);
        // 평균 평점이 높은 상위 N개의 여행지를 조회하여 반환합니다.
        return destinationRepository.findTopNDestinationsByAverageRating(topNPageable);
    }


    /**
     * 사용자가 평가한 여행지와 평점을 반환합니다.
     * @param userId 사용자 ID
     * @return List<UserDestination> 사용자의 여행지 평가 목록
     */
    public List<UserDestination> getUserInteractions(Long userId) {
        return userDestinationRepository.findByUserId(userId);
    }

    /**
     * 모든 여행지의 특성을 반환합니다.
     * @return List<Destination> 여행지 목록
     */
    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    /**
     * 사용자의 프로필과 여행지 특성 간의 유사도를 계산하여 여행지를 추천합니다.
     * @param userId 사용자 ID
     * @return List<Destination> 추천 여행지 목록
     */
//    public List<Destination> recommendDestinations(Long userId) {
//        User user = getUserById(userId);
//        List<UserDestination> interactions = getUserInteractions(userId);
//        List<Destination> allDestinations = getAllDestinations();
//
//        // 1. 사용자 프로필 생성
//        // 여기서는 간단하게 사용자의 평균 평점을 사용합니다.
//        double averageRating = interactions.stream()
//                .mapToInt(UserDestination::getRating)
//                .average()
//                .orElse(0.0);
//
//        // 2. 각 여행지의 특성과 사용자 프로필 간의 유사도 계산
//        // 여기서는 간단하게 여행지의 평균 평점과 사용자의 평균 평점 간의 차이를 사용합니다.
//        List<Destination> recommendedDestinations = allDestinations.stream()
//                .filter(destination -> Math.abs(destination.getAverageRating() - averageRating) < 1) // 1점 차이 이내의 여행지만 추천
//                .collect(Collectors.toList());
//
//        return recommendedDestinations;
//    }

}
