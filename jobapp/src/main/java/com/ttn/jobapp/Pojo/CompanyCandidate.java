/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Pojo;

import jakarta.persistence.*;
import lombok.Data;


/**
 *
 * @author Win11
 */
@Entity
@Table(name = "company_candidate", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"candidate_id", "company_id"})
})
@Data
public class CompanyCandidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean saved = true;
}
