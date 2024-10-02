/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

/**
 *
 * @author Win11
 */
@Entity
@Table(name = "account")
@lombok.Getter
@lombok.Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String avatar;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean available = false;

    @Column(nullable = false)
    private String role;
    
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    @JoinColumn
    private Employee employee;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    @JoinColumn
    private Employer employer;
    
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<JobCandidate> jobCandidate;
    
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Job> jobs;

}
