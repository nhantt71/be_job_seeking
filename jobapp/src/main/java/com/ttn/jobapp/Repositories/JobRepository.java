/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Repositories;

import com.ttn.jobapp.Pojo.Job;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Win11
 */
@Repository
@Transactional
public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("SELECT j FROM Job j WHERE LOWER(j.name) LIKE %:keyword% OR LOWER(j.detail) LIKE %:keyword%")
    List<Job> findAllByKeyword(@Param("keyword") String keyword);

    @Query("SELECT j FROM Job j WHERE j.category.id = :categoryId")
    List<Job> findAllByCategory(@Param("categoryId") Long categoryId);

    @Query("SELECT j FROM Job j WHERE j.company.address.province LIKE %:province%")
    List<Job> findAllByProvince(@Param("province") String province);
    
    @Query("SELECT j FROM Job j WHERE j.company.id = :companyId")
    List<Job> findAllByCompany(@Param("companyId") Long companyId);
    
    @Query("SELECT j FROM Job j WHERE j.createdDate >= :sevenDaysAgo")
    List<Job> findRecentJobs(@Param("sevenDaysAgo") LocalDate sevenDaysAgo);
    
    @Query("SELECT j FROM Job j WHERE (LOWER(j.name) LIKE %:keyword% OR LOWER(j.detail) LIKE %:keyword%) AND j.id != :jobId")
    List<Job> findRelatedJobsByKeyword(@Param("keyword") String keyword, @Param("jobId") Long jobId);

    @Query("SELECT j FROM Job j WHERE (LOWER(j.name) LIKE %:keyword% OR LOWER(j.detail) LIKE %:keyword%) AND j.company.id != :companyId")
    List<Job> findCompanyJobsByKeyword(@Param("keyword") String keyword, @Param("companyId") Long companyId);
    
}
