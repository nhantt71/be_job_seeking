/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Pojo.VerificationToken;
import com.ttn.jobapp.Repositories.VerificationTokenRepository;
import com.ttn.jobapp.Services.VerificationTokenService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Win11
 */
@Service
public class VerificationTokenServiceImpl implements VerificationTokenService{
    @Autowired
    private VerificationTokenRepository vtr;

    @Override
    public VerificationToken save(VerificationToken vt) {
        return vtr.save(vt);
    }

    @Override
    public void delete(Long id) {
        vtr.deleteById(id);
    }

    @Override
    public List<VerificationToken> getVerificationTokens() {
        return vtr.findAll();
    }
    
}
