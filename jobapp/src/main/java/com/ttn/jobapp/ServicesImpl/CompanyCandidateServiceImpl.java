/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Pojo.CompanyCandidate;
import com.ttn.jobapp.Repositories.CandidateRepository;
import com.ttn.jobapp.Repositories.CompanyCandidateRepository;
import com.ttn.jobapp.Repositories.CompanyRepository;
import com.ttn.jobapp.Services.CompanyCandidateService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Win11
 */
@Service
public class CompanyCandidateServiceImpl implements CompanyCandidateService {

    @Autowired
    private CompanyCandidateRepository ccr;

    @Autowired
    private CandidateRepository cr;

    @Autowired
    private CompanyRepository comR;

    @Override
    public CompanyCandidate save(CompanyCandidate cc) {
        return ccr.save(cc);
    }

    @Override
    public List<CompanyCandidate> getCompanyCandidates() {
        return ccr.findAll();
    }

    @Override
    public void delete(Long id) {
        ccr.deleteById(id);
    }

    @Override
    public Boolean checkSavedStatus(Long candidateId, Long companyId) {
        return this.ccr.getCompanyCandidateByCCId(candidateId, companyId) != null;
    }

    @Override
    public CompanyCandidate saveCandidate(Long candidateId, Long companyId) {
        CompanyCandidate cc = this.ccr.getCompanyCandidateByCCId(candidateId, companyId);

        if (cc == null) {
            cc = new CompanyCandidate();
            cc.setCandidate(this.cr.findById(candidateId).orElse(null));
            cc.setCompany(this.comR.findById(companyId).orElse(null));
        }

        cc.setSaved(Boolean.TRUE);
        return this.ccr.save(cc);
    }

    @Override
    public CompanyCandidate unsaveCandidate(Long candidateId, Long companyId) {
        CompanyCandidate cc = this.ccr.getCompanyCandidateByCCId(candidateId, companyId);

        if (cc != null) {
            cc.setSaved(Boolean.FALSE);
            this.ccr.save(cc);
        }

        return cc;
    }

    @Override
    public List<CompanyCandidate> getSavedCandidateByCompany(Long companyId) {
        return this.ccr.getSavedCandidateByCompany(companyId);
    }

}
