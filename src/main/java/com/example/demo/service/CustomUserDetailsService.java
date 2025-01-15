package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자명으로 사용자 정보를 조회합니다.
        User user = userRepository.findByUserid(username);
        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        // Spring Security가 사용할 수 있는 UserDetails 객체를 반환합니다.
        return new org.springframework.security.core.userdetails.User(user.getUserid(), user.getPassword(), getAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        // 사용자의 권한을 가져옵니다.
        String userRole = user.getRole().getName().name();
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRole);
        return authorities;
    }
}
