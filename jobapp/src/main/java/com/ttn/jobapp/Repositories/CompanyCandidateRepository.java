/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Repositories;

import com.ttn.jobapp.Pojo.CompanyCandidate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Win11
 */
public interface CompanyCandidateRepository extends JpaRepository<CompanyCandidate, Long>{
    
}
