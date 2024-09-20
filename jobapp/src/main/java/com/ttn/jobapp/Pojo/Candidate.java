/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Pojo;

/**
 *
 * @author Win11
 */
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@lombok.Getter
@lombok.Setter
@Table(name = "candidate")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDate birth;
    
    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @PrePersist
    @PreUpdate
    public void validateGender() {
        if (!gender.equals("Male") && !gender.equals("Female") && !gender.equals("Other")) {
            throw new IllegalArgumentException("Invalid gender value. Must be 'Male', 'Female', or 'Other'.");
        }
    }
    
    private String nationality;
    private String experience;
    
    @Column(name = "desired_industry")
    private String desiredIndustry;
    
    @Column(name = "desired_job")
    private String desiredJob;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean remoteWork = false;
    
    @Column(name = "languages_skill", columnDefinition = "TEXT")
    private String languagesSkill;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_id", unique = true)
    private Account account;

}