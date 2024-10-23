/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Repositories;

import com.ttn.jobapp.Pojo.Candidate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Win11
 */
@Repository
@Transactional
public interface CandidateRepository extends JpaRepository<Candidate, Long>{
    @Query("SELECT c FROM Candidate c WHERE c.account.email LIKE :email")
    Candidate getCandidateByEmail(@Param("email") String email);
    
    @Query("SELECT c FROM Candidate c LEFT JOIN JobCandidate jc ON c.id = jc.candidate.id WHERE jc.candidate IS NULL")
    List<Candidate> findUnattachedCandidates();
    
    @Query("SELECT c FROM Candidate c LEFT JOIN CompanyCandidate cc ON c.id = cc.candidate.id WHERE cc.candidate IS NULL")
    List<Candidate> findCandidatesWithoutCompany();
}

