/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Pojo;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author MyLaptop
 */
@Entity
@Table(name = "job_candidate")
@Data
public class JobCandidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false, unique = true)
    private Job job;

    @Column(name = "saved_at", nullable = false)
    private LocalDateTime savedAt;
    
}
