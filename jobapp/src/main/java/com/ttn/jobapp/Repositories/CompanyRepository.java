/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Repositories;

import com.ttn.jobapp.Pojo.Company;
import java.time.LocalDateTime;
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
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("SELECT c FROM Company c WHERE LOWER(c.name) LIKE %:keyword%")
    List<Company> getFindingCompanies(@Param("keyword") String keyword);

    @Query("SELECT c FROM Company c WHERE c.recruiter.id = :recruiterId")
    Company getCompanyByRecruiterId(@Param("recruiterId") Long recruiterId);

    @Query(value = "SELECT * FROM companies WHERE createdDate < :cutoffDate ORDER BY createdDate DESC",
            nativeQuery = true)
    List<Company> findByCreatedDateBefore(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    @Query("SELECT c FROM Company c WHERE c.recruiter.account.verified = true "
            + "AND c.reviewStatus = 'PENDING'")
    List<Company> findByPendingVerifiedAccount();
}
