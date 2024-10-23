/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;

import com.ttn.jobapp.Pojo.VerificationToken;
import java.util.List;

/**
 *
 * @author Win11
 */
public interface VerificationTokenService {
    VerificationToken save(VerificationToken vt);
    
    void delete(Long id);
    
    List<VerificationToken> getVerificationTokens();
}
