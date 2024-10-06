/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Pojo.Employee;
import com.ttn.jobapp.Repositories.EmployeeRepository;
import com.ttn.jobapp.Services.EmployeeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Win11
 */
@Service
public class EmployeeServiceImpl implements EmployeeService{
    
    @Autowired
    private EmployeeRepository er;

    @Override
    public Employee save(Employee employee) {
        return er.save(employee);
    }

    @Override
    public List<Employee> getEmployees() {
        return er.findAll();
    }

    @Override
    public void delete(Long id) {
        er.deleteById(id);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return er.findById(id).get();
    }

}
