/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;

import com.ttn.jobapp.Pojo.TemporaryRecruiter;
import java.util.List;

/**
 *
 * @author Win11
 */
public interface TemporaryRecruiterService {
    TemporaryRecruiter save(TemporaryRecruiter tempRecruiter);
    
    List<TemporaryRecruiter> getTempRecruiter();
    
    void delete(Long id);
}
