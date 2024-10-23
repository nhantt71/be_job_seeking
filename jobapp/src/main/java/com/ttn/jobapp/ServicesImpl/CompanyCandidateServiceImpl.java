/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Pojo.CompanyCandidate;
import com.ttn.jobapp.Repositories.CompanyCandidateRepository;
import com.ttn.jobapp.Services.CompanyCandidateService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Win11
 */
@Service
public class CompanyCandidateServiceImpl implements CompanyCandidateService{
    
    @Autowired
    private CompanyCandidateRepository ccr;

    @Override
    public CompanyCandidate save(CompanyCandidate cc) {
        return ccr.save(cc);
    }

    @Override
    public List<CompanyCandidate> getCompanyCandidates() {
        return ccr.findAll();
    }

    @Override
    public void delete(Long id) {
        ccr.deleteById(id);
    }
    
}
