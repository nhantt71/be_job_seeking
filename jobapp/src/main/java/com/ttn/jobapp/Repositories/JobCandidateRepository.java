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
public interface JobCandidateRepository extends JpaRepository<JobCandidate, Long> {

    @Query("SELECT jc FROM JobCandidate jc WHERE jc.candidate.id = :candidateId")
    List<JobCandidate> getJobsByCandidateId(@Param("candidateId") Long candidateId);

    @Query("SELECT jc FROM JobCandidate jc WHERE jc.candidate.id = :candidateId AND jc.job.id = :jobId")
    JobCandidate getJobCandidateByJobAndCandidate(@Param("candidateId") Long candidateId, @Param("jobId") Long jobId);

    @Query("SELECT jc FROM JobCandidate jc WHERE jc.candidate.id = :candidateId AND jc.applied = true")
    List<JobCandidate> getAppliedJobs(@Param("candidateId") Long candidateId);

    @Query("SELECT jc FROM JobCandidate jc WHERE jc.candidate.id = :candidateId AND jc.saved = true")
    List<JobCandidate> getSavedJobs(@Param("candidateId") Long candidateId);

    @Query("SELECT EXTRACT(MONTH FROM jc.appliedAt) AS month, COUNT(jc) FROM JobCandidate jc "
            + "WHERE EXTRACT(YEAR FROM jc.appliedAt) = :year "
            + "GROUP BY EXTRACT(MONTH FROM jc.appliedAt)")
    List<Object[]> countApplicationsByMonth(@Param("year") int year);

    @Query("SELECT EXTRACT(QUARTER FROM jc.appliedAt) AS quarter, COUNT(jc) FROM JobCandidate jc "
            + "WHERE EXTRACT(YEAR FROM jc.appliedAt) = :year "
            + "GROUP BY EXTRACT(QUARTER FROM jc.appliedAt)")
    List<Object[]> countApplicationsByQuarter(@Param("year") int year);
}
