package com.example.demo.service;

import com.example.demo.entity.Role;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// 권한 서비스 예시
@Service
public class RoleService {@Autowired
private RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role createRole(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }

    public Role getRoleByName(String roleName) {
        return roleRepository.findByName(roleName);
    }

    public Role updateRole(Long roleId, Role updatedRole) {
        Role existingRole = roleRepository.findById(roleId).orElse(null);
        if (existingRole != null) {
            // 업데이트 로직을 구현하고 저장
            existingRole.setName(updatedRole.getName());
            // ...
            return roleRepository.save(existingRole);
        } else {
            return null;
        }
    }

    public boolean deleteRole(Long roleId) {
        if (roleRepository.existsById(roleId)) {
            roleRepository.deleteById(roleId);
            return true;
        } else {
            return false;
        }
    }
}
