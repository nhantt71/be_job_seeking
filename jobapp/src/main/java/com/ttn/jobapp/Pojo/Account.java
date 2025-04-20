/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ttn.jobapp.Utils.AuthProvider;
import com.ttn.jobapp.Utils.Role;
import jakarta.persistence.*;

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

    @Column
    private String avatar;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean verified = false;

    @Column(name = "verify_token")
    private String verifyToken;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    @JoinColumn
    @JsonIgnore
    private Candidate candidate;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    @JoinColumn
    @JsonIgnore
    private Recruiter recruiter;

}

