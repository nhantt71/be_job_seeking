/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;

import com.ttn.jobapp.Dto.CandidateDto;
import com.ttn.jobapp.Pojo.Candidate;
import java.util.List;

/*
 *
 * @author Win11
 */


public interface CandidateService {
    
    Candidate save(Candidate candidate);
    
    List<Candidate> getCandidates();
    
    void delete(Long id);
    
    Candidate getCandidateById(Long id);
    
    Candidate getCandidateByEmail(String email);
    
    List<Candidate> getUnattachCandidates();
    
    List<Candidate> getCandidatesWithoutCompany();
    
    List<Candidate> getCandidatesByIds(List<Long> candidateIds);
    
}
