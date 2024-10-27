/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Repositories;

import com.ttn.jobapp.Pojo.CompanyCandidate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Win11
 */
public interface CompanyCandidateRepository extends JpaRepository<CompanyCandidate, Long>{
    @Query("SELECT cc FROM CompanyCandidate cc WHERE cc.candidate.id = candidateId AND cc.company.id = companyId")
    CompanyCandidate getCompanyCandidateByCCId(@Param("candidateId") Long candidateId,
            @Param("companyId") Long companyId);
    
    @Query("SELECT cc FROM CompanyCandidate cc WHERE cc.company.id = companyId AND cc.saved = true")
    List<CompanyCandidate> getSavedCandidateByCompany(@Param("companyId") Long companyId);
}
