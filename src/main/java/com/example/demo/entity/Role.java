package com.example.demo;


import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    // 다대다 관계로 User 엔티티와 연결
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public void setName(String roleName) {
        this.name = roleName;
    }

    public String getName() {
        return name;
    }


    // 생성자, getter, setter 등 필요한 메서드 추가
}

