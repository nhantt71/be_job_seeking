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

@Entity
@lombok.Getter
@lombok.Setter
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String fullname;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @OneToOne
    @JoinColumn(name = "account_id", unique = true)
    private Account account;

}
