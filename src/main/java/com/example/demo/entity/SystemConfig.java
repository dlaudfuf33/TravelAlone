package com.example.demo.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SystemConfig")
public class SystemConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "basescore")
    private Double basescore;

    @Column(name = "modifiedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @Column(name = "adminId")
    private Long adminId;


    @PrePersist
    @PreUpdate
    public void setModifiedDate() {
        this.modifiedDate = new Date();
    }

    public Double getBasescore() {
        return basescore;
    }

    public void setBasescore(Double basescore) {
        this.basescore = basescore;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
}
