/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Repositories;

import com.ttn.jobapp.Pojo.TemporaryRecruiter;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Win11
 */
public interface TemporaryRecruiterRepository extends JpaRepository<TemporaryRecruiter, Long>{
    @Query("SELECT tr FROM TemporaryRecruiter tr WHERE tr.company.id = companyId AND tr.recruiter.id = recruiterId")
    TemporaryRecruiter getTempRecruiterByIds(@Param("companyId") Long companyId, 
            @Param("recruiterId") Long recruiterId);
    
    @Query("SELECT tr FROM TemporaryRecruiter tr WHERE tr.company.id = companyId")
    List<TemporaryRecruiter> getTempRecruiterByCompany(@Param("companyId") Long companyId);
}
