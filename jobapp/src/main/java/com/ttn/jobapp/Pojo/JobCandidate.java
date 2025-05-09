/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Pojo;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author MyLaptop
 */
@Entity
@Table(name = "job_candidate", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"candidate_id", "job_id"})
})
@Data
public class JobCandidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean saved = false;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean applied = false;

    @Column(name = "saved_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime savedAt;
    
    @Column(name = "applied_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime appliedAt;

}
