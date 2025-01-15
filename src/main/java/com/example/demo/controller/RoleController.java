package com.example.demo.controller;// RoleController.java

import com.example.demo.entity.Role;
import com.example.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Role API", description = "역할(Role) 관련 API 엔드포인트")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {


    private final RoleService roleService;

    @Operation(summary = "모든 역할을 조회합니다.")
    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @Operation(summary = "특정 ID의 역할을  조회합니다.")
    @GetMapping("/read/{roleName}")
    public ResponseEntity<Role> getRoleByName(@PathVariable Role.RoleName roleName) {
        Role role = roleService.getRoleByName(roleName);
        if (role != null) {
            return ResponseEntity.ok(role);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "특정 ID의 역할을 삭제합니다.")
    @DeleteMapping("/delete/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable Long roleId) {
        boolean deleted = roleService.deleteRole(roleId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
