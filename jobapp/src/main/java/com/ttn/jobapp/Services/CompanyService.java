/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;

import com.ttn.jobapp.Pojo.Company;
import java.util.List;


public interface CompanyService {

    Company save(Company company);

    List<Company> getCompanies();

    void delete(Long id);
    
    Integer jobAmount(Company company);
    
    Company getCompanyById(Long id);
    
    List<Company> getFindingCompanies(String keyword);
    
    Company getCompanyByRecruiterId(Long recruiterId);
}
