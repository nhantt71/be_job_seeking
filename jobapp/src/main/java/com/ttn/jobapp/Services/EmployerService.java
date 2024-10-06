/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;

import com.ttn.jobapp.Pojo.Employer;
import java.util.List;

/**
 *
 * @author Win11
 */
public interface EmployerService {

    Employer save(Employer employer);

    List<Employer> getEmployers();

    void delete(Long id);
    
    Employer getEmployerById(Long id);
}
