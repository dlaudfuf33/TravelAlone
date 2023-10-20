package com.example.demo;

import javax.persistence.Column;
import java.time.LocalDate;

public class UserRequest {

    private String userid;
    private String password;
    private String email;
    private String profileImageUrl;
    private LocalDate dateOfBirth;
    private String gender;
    private String address; // tmp
    private String city; // 시/도
    private String district; // 군/구
    private String detailedAddress; // 상세 주소    // 다른 필드도 추가할 수 있습니다.ss

    // 게터와 세터 메서드

    public String getUserid() {
        return userid;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
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

    public String getAddress() {
        return address;
    }
}
