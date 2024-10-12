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
@lombok.Getter
@lombok.Setter
@Table(name = "temporary_recruiter", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"company_id", "recruiter_id"})
})
public class TemporaryRecruiter {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "recruiter_id", nullable = false)
    private Recruiter recruiter;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}
