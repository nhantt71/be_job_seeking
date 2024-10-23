/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;

import com.ttn.jobapp.Pojo.Recruiter;
import java.util.List;

/**
 *
 * @author Win11
 */
public interface RecruiterService {

    Recruiter save(Recruiter recruiter);

    List<Recruiter> getRecruiters();

    void delete(Long id);
    
    Recruiter getRecruiterById(Long id);
    
    Recruiter getRecruiterByEmail(String email);
    
    List<Recruiter> getRecruitersWithoutCompanyId();
}
