/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.EmployeeDto;
import com.ttn.jobapp.Pojo.Employee;
import com.ttn.jobapp.Services.EmployeeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Win11
 */
@RestController
@RequestMapping("api/employee")
public class ApiEmployeeController {

    @Autowired
    private EmployeeService es;

    @GetMapping("/get-employees")
    public ResponseEntity<List<Employee>> getEmployees() {
        List<Employee> employees = this.es.getEmployees();
        if (employees.isEmpty()) {
            return new ResponseEntity<>(employees, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(employees, HttpStatus.OK);
        }
    }

    @GetMapping("/get-employee/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable("id") Long id) {
        Employee employee = this.es.getEmployeeById(id);
        EmployeeDto emDto = new EmployeeDto();
        if (employee == null) {
            return new ResponseEntity<>(emDto, HttpStatus.NOT_FOUND);
        } else {
            emDto.setAccountId(employee.getAccount().getId());
            emDto.setFullname(employee.getFullname());
            emDto.setPhoneNumber(employee.getPhoneNumber());
            return new ResponseEntity<>(emDto, HttpStatus.OK);
        }
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<String> editEmployee(@PathVariable("id") Long id, @RequestBody EmployeeDto emDto) {
        Employee employee = this.es.getEmployeeById(id);

        if (employee == null) {
            return new ResponseEntity<>("Employee not found!", HttpStatus.NOT_FOUND);
        } else {
            employee.setFullname(emDto.getFullname());
            employee.setPhoneNumber(emDto.getPhoneNumber());
            this.es.save(employee);
            return new ResponseEntity<>("Edited employee successfully!", HttpStatus.OK);
        }
    }
}
