package com.example.demo.service;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // Spring Security에서 사용자 정보를 불러오는 메서드
    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        // 사용자명(username)을 이용하여 UserRepository에서 사용자 정보를 조회
        User user = userRepository.findByUserid(userid);

        // 사용자 정보가 없을 경우, 예외를 던져 사용자를 찾을 수 없다고 알림
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + userid);
        }

        // User 객체를 UserDetails로 변환하여 반환
        return (UserDetails) user;
    }
}
