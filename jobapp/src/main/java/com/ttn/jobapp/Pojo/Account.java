/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Pojo;

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
    
    private String avatar;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean available = true;
    
    @Column(nullable = false)
    private String role;
    
}
