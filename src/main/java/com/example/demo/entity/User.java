package com.example.demo.entity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Period;
import java.util.Collection;

@Entity
@Table(name = "users") // "users" 테이블과 매핑
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userid;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;
//testss

    @Column(unique = true)
//    @Email(message = "유효한 이메일 형식이어야 합니다.")
    private String email;
    @Column
    private String city; // 시/도
    @Column
    private String district; // 군/구
    @Column
    private String detailedAddress; // 상세 주소
    @Column
    private LocalDateTime registrationDate; // 가입일
    @Column
    private LocalDate dateOfBirth; // 생년월일
    @Column
    private String gender; // 성별
    @Column
    private String salt; // 소금

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;


    /**
     * 사용자의 고유 식별자를 반환합니다.
     * @return 사용자 ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 사용자 이름을 반환합니다.
     * @return 사용자 이름
     */
    public String getUserid() {
        return userid;
    }

    /**
     * 사용자의 비밀번호를 반환합니다.
     * @return 비밀번호
     */
    public String getPassword() {
        return password;
    }

    /**
     * 사용자의 이메일 주소를 반환합니다.
     * @return 이메일 주소
     */
    public String getEmail() {
        return email;
    }

    /**
     * 사용자의 가입일을 반환합니다.
     * @return 가입일
     */
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    /**
     * 사용자의 생년월일을 반환합니다.
     * @return 생년월일
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * 사용자의 성별을 반환합니다.
     * @return 성별
     */
    public String getGender() {
        return gender;
    }
    /**
     * 사용자의 소금을 반환합니다.
     * @return 소금
     */
    public String getSalt() {
        return salt;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public int getAge() {
        return calculateAge();
    }
    public int calculateAge() {
        if (dateOfBirth != null) {
            LocalDate currentDate = LocalDate.now();
            Period period = Period.between(dateOfBirth, currentDate);
            return period.getYears();
        } else {
            return 0; // 생년월일이 없으면 0을 반환하거나 다른 값을 선택할 수 있습니다.
        }
    }

    public String getAgeRange() {
        int age = calculateAge();
        if (age < 20) {
            return "10";
        } else if (age < 30) {
            return "20";
        } else if (age < 40) {
            return "30";
        } else if (age < 50) {
            return "40";
        } else if (age < 60) {
            return "50";
        } else {
            return "60이상";
        }
    }


    /**
     * 사용자의 성별을 설정합니다.
     * @param gender 성별
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * 사용자의 사용자 이름을 설정합니다.
     * @param userid 사용자 이름
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * 사용자의 비밀번호를 설정합니다.
     * @param password 비밀번호
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 사용자의 이메일 주소를 설정합니다.
     * @param email 이메일 주소
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 사용자의 가입일을 설정합니다.
     * @param registrationDate 가입일
     */
    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * 사용자의 생년월일을 설정합니다.
     * @param dateOfBirth 생년월일
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    /**
     * 사용자의 소금을 설정합니다.
     * @param salt 소금
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role rootRole) {
        this.role=rootRole;
    }


}

