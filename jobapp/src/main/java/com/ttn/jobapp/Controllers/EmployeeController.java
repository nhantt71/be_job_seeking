/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Pojo.Employee;
import com.ttn.jobapp.Services.EmployeeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Win11
 */
@RestController
@RequestMapping("/api")
public class EmployeeController {
    
    @Autowired
    private EmployeeService es;
    
    @RequestMapping("/get-employees")
    @GetMapping
    public ResponseEntity<List<Employee>> getEmployees(){
        List<Employee> employees = this.es.getEmployees();
        if (employees.isEmpty()){
            return new ResponseEntity<>(employees, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(employees, HttpStatus.OK);
        }
    }
}
