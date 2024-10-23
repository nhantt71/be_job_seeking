/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Pojo;

/**
 *
 * @author Win11
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@lombok.Getter
@lombok.Setter
@Table(name = "recruiter")
public class Recruiter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String city;
    private String province;

    @Column(nullable = false)
    private String fullname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @PrePersist
    @PreUpdate
    public void validateGender() {
        if (!gender.equals("Male") && !gender.equals("Female") && !gender.equals("Other")) {
            throw new IllegalArgumentException("Invalid gender value. Must be 'Male', 'Female', or 'Other'.");
        }
    }
    
    @OneToOne(mappedBy = "recruiter")
    @JsonIgnore
    private Company companyCreater;

    @OneToOne
    @JoinColumn(name = "account_id", unique = true)
    @JsonIgnore
    private Account account;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "company_id", nullable = true)
    private Company company;
   
    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Job> jobs;
    
    @OneToOne(mappedBy = "recruiter", cascade = CascadeType.ALL)
    @JsonIgnore
    private TemporaryRecruiter temporaryRecruiter;

}
