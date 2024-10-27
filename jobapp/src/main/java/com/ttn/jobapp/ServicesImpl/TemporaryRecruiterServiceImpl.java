/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Pojo.TemporaryRecruiter;
import com.ttn.jobapp.Repositories.TemporaryRecruiterRepository;
import com.ttn.jobapp.Services.TemporaryRecruiterService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Win11
 */
@Service
public class TemporaryRecruiterServiceImpl implements TemporaryRecruiterService{
    
    @Autowired
    private TemporaryRecruiterRepository tempRecruiterRepo;

    @Override
    public TemporaryRecruiter save(TemporaryRecruiter tempRecruiter) {
        return this.tempRecruiterRepo.save(tempRecruiter);
    }

    @Override
    public List<TemporaryRecruiter> getTempRecruiter() {
        return this.tempRecruiterRepo.findAll();
    }

    @Override
    public void delete(Long id) {
        this.tempRecruiterRepo.deleteById(id);
    }

    @Override
    public TemporaryRecruiter getTempRecruiterByIds(Long companyId, Long recruiterId) {
        return this.tempRecruiterRepo.getTempRecruiterByIds(companyId, recruiterId);
    }

    @Override
    public List<TemporaryRecruiter> getTempRecruiterByCompany(Long companyId) {
        return this.tempRecruiterRepo.getTempRecruiterByCompany(companyId);
    }
    
}
