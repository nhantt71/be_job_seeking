/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Pojo.Account;
import com.ttn.jobapp.Pojo.Company;
import com.ttn.jobapp.Repositories.AccountRepository;
import com.ttn.jobapp.Repositories.CompanyRepository;
import com.ttn.jobapp.Repositories.RecruiterRepository;
import com.ttn.jobapp.Services.CompanyService;
import com.ttn.jobapp.Utils.ReviewStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Win11
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository cr;

    @Autowired
    private AccountRepository ar;

    @Autowired
    private RecruiterRepository rr;

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
    public Company getCompanyByRecruiterId(Long recruiterId) {
        return this.cr.getCompanyByRecruiterId(recruiterId);
    }

    @Override
    @Scheduled(cron = "0 0 3 * * ?") // Runs daily at 3 AM
    @Transactional
    public void cleanupUnverifiedCompanies() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(7); // 7-day grace period

        List<Company> companies = cr.findByCreatedDateBefore(cutoffDate);

        companies.forEach(company -> {
            Long accountId = company.getRecruiter().getAccount().getId();
            Optional<Account> account = ar.findById(accountId);

            if (account.isPresent() && !account.get().getVerified()) {
                ar.delete(company.getRecruiter().getAccount());
                rr.delete(company.getRecruiter());
                cr.delete(company);
                log.info("Deleted company {} created by unverified account recruiter {}",
                        company.getId(), company.getRecruiter());
            }
        });
    }

    @Override
    public List<Company> findByPendingVerifiedAccount() {
        return this.cr.findByPendingVerifiedAccount();
    }

    @Override
    public Company processApproval(Long companyId, String action) {
        Optional<Company> company = this.cr.findById(companyId);
        
        company.get().setReviewStatus(ReviewStatus.valueOf(action.toUpperCase()));
        
        return this.cr.save(company.get());
    }

}
