/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Pojo.Employer;
import com.ttn.jobapp.Repositories.EmployerRepository;
import com.ttn.jobapp.Services.EmployerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Win11
 */
@Service
public class EmployerServiceImpl implements EmployerService{
    
    @Autowired
    private EmployerRepository er;

    @Override
    public Employer save(Employer employer) {
        return er.save(employer);
    }

    @Override
    public List<Employer> getEmployers() {
        return er.findAll();
    }

    @Override
    public void delete(Long id) {
        er.deleteById(id);
    }

    @Override
    public Employer getEmployerById(Long id) {
        return er.findById(id).get();
    }
    
}
