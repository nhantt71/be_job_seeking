/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.CompanyDto;
import com.ttn.jobapp.Pojo.Company;
import com.ttn.jobapp.Services.CompanyService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Win11
 */
@RestController
@RequestMapping("api/company")
@CrossOrigin
public class ApiCompanyController {
    
    @Autowired
    private CompanyService cs;
    
    @GetMapping
    public ResponseEntity<List<CompanyDto>> allCompanies(){
        List<Company> companies = this.cs.getCompanies();
        
        List<CompanyDto> comDto = new ArrayList<>();
        
        companies.forEach(x -> {
            CompanyDto c = new CompanyDto();
            
            c.setId(x.getId());
            c.setName(x.getName());
            c.setLogo(x.getLogo());
            c.setJobAmount(this.cs.jobAmount(x));
            
            comDto.add(c);
        });
       
        return new ResponseEntity<>(comDto, HttpStatus.OK);
    }
    
}
