package com.example.demo;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users") // "users" 테이블과 매핑
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    private String profileImageUrl; // 프로필 이미지 URL

    @Column(unique = true)
    private String nickname; // 닉네임

    private LocalDateTime registrationDate; // 가입일

    private String introduction; // 사용자 소개

    private LocalDate dateOfBirth; // 생년월일

    private String gender; // 성별

    private String salt; // 소금
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    // 생성자, getter, setter, 기타 메서드 등 추가

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
    public String getUsername() {
        return username;
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
     * 사용자의 프로필 이미지 URL을 반환합니다.
     * @return 프로필 이미지 URL
     */
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    /**
     * 사용자의 닉네임을 반환합니다.
     * @return 닉네임
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 사용자의 가입일을 반환합니다.
     * @return 가입일
     */
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    /**
     * 사용자의 소개를 반환합니다.
     * @return 사용자 소개
     */
    public String getIntroduction() {
        return introduction;
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
    /**
     * 사용자의 성별을 설정합니다.
     * @param gender 성별
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * 사용자의 사용자 이름을 설정합니다.
     * @param username 사용자 이름
     */
    public void setUsername(String username) {
        this.username = username;
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
     * 사용자의 프로필 이미지 URL을 설정합니다.
     * @param profileImageUrl 프로필 이미지 URL
     */
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    /**
     * 사용자의 닉네임을 설정합니다.
     * @param nickname 닉네임
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 사용자의 가입일을 설정합니다.
     * @param registrationDate 가입일
     */
    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * 사용자의 자기 소개를 설정합니다.
     * @param introduction 자기 소개
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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

    public Collection<Role> getRoles() {
        return roles;
    }
}

