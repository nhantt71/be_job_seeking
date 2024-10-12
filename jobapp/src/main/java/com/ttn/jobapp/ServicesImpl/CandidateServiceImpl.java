/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Pojo.Candidate;
import com.ttn.jobapp.Repositories.CandidateRepository;
import com.ttn.jobapp.Services.CandidateService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Win11
 */
@Service
public class CandidateServiceImpl implements CandidateService{
    
    @Autowired
    private CandidateRepository cr;

    @Override
    public Candidate save(Candidate candidate) {
        return cr.save(candidate);
    }

    @Override
    public List<Candidate> getCandidates() {
        return cr.findAll();
    }

    @Override
    public void delete(Long id) {
        cr.deleteById(id);
    }

    @Override
    public Candidate getCandidateById(Long id) {
        return cr.findById(id).get();
    }

    @Override
    public Candidate getCandidateByEmail(String email) {
        return cr.getCandidateByEmail(email);
    }

}
