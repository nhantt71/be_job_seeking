/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;

import com.ttn.jobapp.Pojo.CompanyCandidate;
import java.util.List;

/**
 *
 * @author Win11
 */
public interface CompanyCandidateService {
    CompanyCandidate save(CompanyCandidate cc);
    
    List<CompanyCandidate> getCompanyCandidates();
    
    void delete(Long id);
}
