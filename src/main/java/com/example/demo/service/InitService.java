package com.example.demo.service;


import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Service
public class InitService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct//앱시작시 루트계정을 생성하는 함수
    public void init() {
        Role rootRole = roleRepository.findByName(Role.RoleName.ROOTADMIN);
        if (rootRole == null) {
            // 없으면 Role 및 root 관리자 생성
            rootRole = new Role();
            rootRole.setName(Role.RoleName.ROOTADMIN.name());
            roleRepository.save(rootRole);

            User rootAdmin = new User();
            rootAdmin.setUserid("RTADM");
            rootAdmin.setPassword("1q2w3e4r"); // 실제로는 암호화된 비밀번호를 사용해야 합니다.
            rootAdmin.setRole(rootRole);
            userRepository.save(rootAdmin);
        }
    }
}
