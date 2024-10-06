/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.EmployerDto;
import com.ttn.jobapp.Pojo.Employer;
import com.ttn.jobapp.Services.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Win11
 */
@RestController
@CrossOrigin
@RequestMapping("api/employer")
public class ApiEmployerController {

    @Autowired
    private EmployerService es;

    @GetMapping("/get-employer/{id}")
    public ResponseEntity<EmployerDto> getEmployee(@PathVariable("id") Long id) {
        Employer employer = this.es.getEmployerById(id);
        EmployerDto emDto = new EmployerDto();
        if (employer == null) {
            return new ResponseEntity<>(emDto, HttpStatus.NOT_FOUND);
        } else {
            emDto.setAccountId(employer.getAccount().getId());
            emDto.setFullname(employer.getFullname());
            emDto.setPhoneNumber(employer.getPhoneNumber());
            emDto.setCity(employer.getCity());
            emDto.setCompanyId(employer.getCompany().getId());
            emDto.setGender(employer.getGender());
            emDto.setProvince(employer.getProvince());
            return new ResponseEntity<>(emDto, HttpStatus.OK);
        }
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<String> editEmployer(@PathVariable("id") Long id, @RequestBody EmployerDto emDto) {
        Employer employer = this.es.getEmployerById(id);

        if (employer == null) {
            return new ResponseEntity<>("Employer not found!", HttpStatus.NOT_FOUND);
        } else {
            employer.setFullname(emDto.getFullname());
            employer.setPhoneNumber(emDto.getPhoneNumber());
            employer.setFullname(emDto.getFullname());
            employer.setPhoneNumber(emDto.getPhoneNumber());
            employer.setCity(emDto.getCity());
            employer.setGender(emDto.getGender());
            employer.setProvince(emDto.getProvince());
            this.es.save(employer);
            return new ResponseEntity<>("Edited employer successfully!", HttpStatus.OK);
        }
    }
}
