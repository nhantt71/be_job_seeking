/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Pojo.JobCandidate;
import com.ttn.jobapp.Repositories.JobCandidateRepository;
import com.ttn.jobapp.Services.JobCandidateService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author MyLaptop
 */
@Service
public class JobCandidateServiceImpl implements JobCandidateService{

    @Autowired
    private JobCandidateRepository jcr;
    
    @Override
    public JobCandidate save(JobCandidate jobCandidate) {
        return this.jcr.save(jobCandidate);
    }

    @Override
    public List<JobCandidate> getJobCandidates() {
        return this.jcr.findAll();
    }

    @Override
    public void delete(Long id) {
        this.jcr.deleteById(id);
    }

    @Override
    public List<JobCandidate> getJobsByCandidateId(Long id) {
        return this.jcr.getJobsByCandidateId(id);
    }

    @Override
    public JobCandidate getJobCandidateByJobAndCandidate(Long accountId, Long jobId) {
        return this.jcr.getJobCandidateByJobAndCandidate(accountId, jobId);
    }

    @Override
    public List<JobCandidate> getAppliedJobs(Long candidateId) {
        return this.jcr.getAppliedJobs(candidateId);
    }

    @Override
    public List<JobCandidate> getSavedJobs(Long candidateId) {
        return this.jcr.getSavedJobs(candidateId);
    }
    
}
