/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Pojo.Recruiter;
import com.ttn.jobapp.Repositories.RecruiterRepository;
import com.ttn.jobapp.Services.RecruiterService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Win11
 */
@Service
public class RecruiterServiceImpl implements RecruiterService{
    
    @Autowired
    private RecruiterRepository er;

    @Override
    public Recruiter save(Recruiter recruiter) {
        return er.save(recruiter);
    }

    @Override
    public List<Recruiter> getRecruiters() {
        return er.findAll();
    }

    @Override
    public void delete(Long id) {
        er.deleteById(id);
    }

    @Override
    public Recruiter getRecruiterById(Long id) {
        return er.findById(id).get();
    }

    @Override
    public Recruiter getRecruiterByEmail(String email) {
        return er.getRecruiterByEmail(email);
    }

    @Override
    public List<Recruiter> getRecruitersWithoutCompanyId() {
        return er.findRecruitersWithoutCompanyId();
    }
    
}
