/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ttn.jobapp.Utils.ReviewStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Win11
 */
@Entity
@lombok.Getter
@lombok.Setter
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "tax_code", nullable = false, unique = true)
    private String taxCode;
    
    private String logo;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "website")
    private String website;
    
    @Column(name = "information", columnDefinition = "TEXT")
    private String information;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;
    
    @OneToOne
    @JoinColumn(name = "created_recruiter_id", nullable = true)
    private Recruiter recruiter;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", nullable = true)
    private Address address;
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Job> jobs;
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CompanyCandidate> companyCandidate;

}
