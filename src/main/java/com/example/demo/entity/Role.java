package com.example.demo.entity;


import javax.annotation.Resource;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    public enum RoleName {
        ROOTADMIN, useadmin0,useadmin1,useadmin2, admin0,admin1,admin2,
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleName name;

    // 1:1 관계로 User 엔티티와 연결
    @OneToOne(mappedBy = "role")
    private User user;

    public void setName(String rootadmin) {
        this.name= RoleName.valueOf(rootadmin);
    }

    public RoleName getName() {
        return name;
    }
}

