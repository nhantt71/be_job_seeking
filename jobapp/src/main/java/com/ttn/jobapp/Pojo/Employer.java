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
    
    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @PrePersist
    @PreUpdate
    public void validateGender() {
        if (!gender.equals("Male") && !gender.equals("Female") && !gender.equals("Other")) {
            throw new IllegalArgumentException("Invalid gender value. Must be 'Male', 'Female', or 'Other'.");
        }
    }

    @OneToOne
    @JoinColumn(name = "account_id", unique = true)
    private Account account;

}
