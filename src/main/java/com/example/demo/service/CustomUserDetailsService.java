package com.example.demo.service;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
            throw new UsernameNotFoundException("해당 사용자를 찾을수 없습니다. userid: " + userid);
        }

        // 사용자의 Role을 GrantedAuthority 목록으로 변환
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().name()));

        return new org.springframework.security.core.userdetails.User(user.getUserid(), user.getPassword(), authorities);
    }
}
