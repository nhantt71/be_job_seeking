/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Repositories;

import com.ttn.jobapp.Pojo.JobCandidate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author MyLaptop
 */
@Repository
@Transactional
public interface JobCandidateRepository extends JpaRepository<JobCandidate, Long>{
    
    @Query("SELECT jc FROM JobCandidate jc WHERE jc.account.id = :accountId")
    List<JobCandidate> getJobsByAccountId(@Param("accountId") Long accountId);
    
    @Query("SELECT jc FROM JobCandidate jc WHERE jc.account.id = :accountId AND jc.job.id = :jobId")
    JobCandidate getJobCandidateByJobAndCandidate(@Param("accountId") Long accountId, @Param("jobId") Long jobId);
    
    @Query("SELECT jc FROM JobCandidate jc WHERE jc.account.id = :accountId AND jc.applied = true")
    List<JobCandidate> getAppliedJobs(@Param("accountId") Long accountId);
    
    @Query("SELECT jc FROM JobCandidate jc WHERE jc.account.id = :accountId AND jc.saved = true")
    List<JobCandidate> getSavedJobs(@Param("accountId") Long accountId);
    
}
