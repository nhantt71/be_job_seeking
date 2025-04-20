/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;

import com.ttn.jobapp.Pojo.JobCandidate;
import java.util.List;

/**
 *
 * @author MyLaptop
 */
public interface JobCandidateService {
    
    JobCandidate save(JobCandidate jobCandidate);

    List<JobCandidate> getJobCandidates();

    void delete(Long id);
    
    List<JobCandidate> getJobsByCandidateId(Long id);
    
    JobCandidate getJobCandidateByJobAndCandidate(Long candidateId, Long jobId);
    
    List<JobCandidate> getAppliedJobs(Long candidateId);
    
    List<JobCandidate> getSavedJobs(Long candidateId);
}
