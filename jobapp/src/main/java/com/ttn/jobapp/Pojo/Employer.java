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
@Table(name = "employer")
public class Employer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String companyName;
    
    private String city;
    private String province;
    
    @Column(columnDefinition = "VARCHAR(10) CHECK (gender IN ('Male', 'Female', 'Other'))")
    private String gender;

    @OneToOne
    @JoinColumn(name = "employee_id", unique = true)
    private Employee employee;

}
