/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Repositories;

import com.ttn.jobapp.Pojo.Recruiter;
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
public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {

    @Query("SELECT r FROM Recruiter r WHERE r.account.email LIKE :email")
    Recruiter getRecruiterByEmail(@Param("email") String email);

    @Query("SELECT r FROM Recruiter r WHERE r.company.id IS NULL")
    List<Recruiter> findRecruitersWithoutCompanyId();
    
    @Query("SELECT r FROM Recruiter r WHERE r.company.id = :companyId")
    List<Recruiter> getRecruitersByCompany(@Param("companyId") Long companyId);
}
