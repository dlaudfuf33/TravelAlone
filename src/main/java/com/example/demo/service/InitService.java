package com.example.demo.service;


import com.example.demo.SaltGenerator;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Service
public class InitService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct//앱시작시 루트계정을 생성하는 함수
    public void init() {
        Role rootRole = roleRepository.findByName(Role.RoleName.ROOTADMIN);
            User rootAdmin = new User();
            String rooterPassword="1q2w3e4r";
            String rooterID="rootadmin";
        if (rootRole == null) {
            // 없으면 Role 및 root 관리자 생성
            rootRole = new Role();
            rootRole.setName(Role.RoleName.ROOTADMIN.name());
            roleRepository.save(rootRole);
            String salt = SaltGenerator.generateSalt();
            // 입력한 패스워드와 소금을 조합하여 해싱
            String saltedPassword = rooterPassword + salt;
            String hashedPassword = passwordEncoder.encode(saltedPassword);

            rootAdmin.setUserid(rooterID);
            rootAdmin.setPassword(hashedPassword);
            rootAdmin.setRole(rootRole);
            userRepository.save(rootAdmin);
        }
    }
}
