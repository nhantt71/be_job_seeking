/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;

import com.ttn.jobapp.Dto.JobDto;
import com.ttn.jobapp.Pojo.Job;
import java.util.List;

/**
 *
 * @author Win11
 */
public interface JobService {

    Job save(Job job);

    List<Job> getJobs();

    void delete(Long id);
    
    List<JobDto> getFindingJobs(String keyword, Long cateId, String province);
    
    Job getJobById(Long id);
    
    List<JobDto> getJobByCompany(Long companyId);
    
    List<JobDto> getJobByCategory(Long categoryId);
    
    List<JobDto> getRecentJobs();
    
    List<JobDto> getRelatedJobsByKeyword(Long jobId, String keyword);
    
    List<JobDto> findCompanyJobsByKeyword(String keyword, Long companyId);
    
    List<JobDto> getAllJobs();
    

}
