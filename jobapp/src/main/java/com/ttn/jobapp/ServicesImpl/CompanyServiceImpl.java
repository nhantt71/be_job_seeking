/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Pojo.Company;
import com.ttn.jobapp.Repositories.CompanyRepository;
import com.ttn.jobapp.Services.CompanyService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Win11
 */
@Service
public class CompanyServiceImpl implements CompanyService{
    
    @Autowired
    private CompanyRepository cr;

    @Override
    public Company save(Company company) {
        return cr.save(company);
    }

    @Override
    public List<Company> getCompanies() {
        return cr.findAll();
    }

    @Override
    public void delete(Long id) {
        cr.deleteById(id);
    }

    @Override
    public Integer jobAmount(Company company) {
        return company.getJobs().size();
    }

    @Override
    public Company getCompanyById(Long id) {
        return this.cr.findById(id).get();
    }

    @Override
    public List<Company> getFindingCompanies(String keyword) {
        return this.cr.getFindingCompanies(keyword);
    }

    @Override
    public List<Company> getUnverifiedCompanies() {
        return cr.findUnverifiedCompaniesWithoutToken();
    }
    
}
