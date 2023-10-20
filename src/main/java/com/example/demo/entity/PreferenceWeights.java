package com.example.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "PreferenceWeights")
public class PreferenceWeights {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ageRange")
    private String ageRange;

    @Column(name = "gender")
    private String gender;

    @Column(name = "season")
    private String season;

    @Column(name = "location")
    private String location;

    @Column(name = "travelType")
    private String travelType;

    @Column(name = "feature")
    private String feature;

    @Column(name = "weight")
    private Double weight;


    // Getter, Setter, 기본 생성자, toString 등의 메서드 추가...

    public void setValuesFrom(PreferenceWeights preference) {
        this.ageRange = preference.ageRange;
        this.gender = preference.gender;
        this.season = preference.season;
        this.location = preference.location;
        this.travelType = preference.travelType;
        this.feature = preference.feature;
        this.weight = preference.weight;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange=ageRange;
    }

    public void setGender(String gender) {
        this.gender=gender;
    }

    public void setSeason(String season) {
        this.season=season;
    }

    public void setLocation(String location) {
        this.location=location;
    }

    public void setTravelType(String travelType) {
        this.travelType=travelType;
    }

    public void setWeight(double weight) {
        this.weight=weight;
    }

    public double getWeight() {
        return this.weight;
    }
}

