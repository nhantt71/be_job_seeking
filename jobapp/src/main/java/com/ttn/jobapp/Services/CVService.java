/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;

import com.ttn.jobapp.Pojo.CV;
import java.util.List;

/**
 *
 * @author Win11
 */

public interface CVService {
    
    CV save(CV cv);

    List<CV> getCVs();

    void delete(Long id);
    
}
