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
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<List<CompanyDto>> allCompanies() {
        List<Company> companies = this.cs.getCompanies();

        List<CompanyDto> comDto = new ArrayList<>();

        companies.forEach(x -> {
            CompanyDto c = new CompanyDto();

            c.setId(x.getId());
            c.setName(x.getName());
            c.setLogo(x.getLogo());
            c.setEmail(x.getEmail());
            c.setInformation(x.getInformation());
            c.setPhoneNumber(x.getPhoneNumber());
            c.setWebsite(x.getWebsite());
            c.setAddressDetail(x.getAddress().getDetail());
            c.setCity(x.getAddress().getCity());
            c.setProvince(x.getAddress().getProvince());
            c.setJobAmount(this.cs.jobAmount(x));

            comDto.add(c);
        });

        return new ResponseEntity<>(comDto, HttpStatus.OK);
    }

    @GetMapping("/get-company-by-id/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable("id") Long id) {
        CompanyDto comDto = new CompanyDto();

        Company com = this.cs.getCompanyById(id);

        comDto.setId(com.getId());
        comDto.setEmail(com.getEmail());
        comDto.setInformation(com.getInformation());
        comDto.setLogo(com.getLogo());
        comDto.setName(com.getName());
        comDto.setPhoneNumber(com.getPhoneNumber());
        comDto.setWebsite(com.getWebsite());
        comDto.setAddressDetail(com.getAddress().getDetail());
        comDto.setCity(com.getAddress().getCity());
        comDto.setProvince(com.getAddress().getProvince());

        return new ResponseEntity<>(comDto, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CompanyDto>> getSearchCompanies(@RequestParam("keyword") String keyword) {
        String keyword1 = keyword;

        if (keyword1 != null) {
            List<Company> companies = this.cs.getFindingCompanies(keyword1.toLowerCase());

            List<CompanyDto> comDto = companies.stream()
                    .map(x -> {
                        CompanyDto c = new CompanyDto();
                        c.setId(x.getId());
                        c.setName(x.getName());
                        c.setLogo(x.getLogo());
                        c.setEmail(x.getEmail());
                        c.setInformation(x.getInformation());
                        c.setPhoneNumber(x.getPhoneNumber());
                        c.setWebsite(x.getWebsite());
                        c.setAddressDetail(x.getAddress().getDetail());
                        c.setCity(x.getAddress().getCity());
                        c.setProvince(x.getAddress().getProvince());
                        c.setJobAmount(this.cs.jobAmount(x));
                        return c;
                    }).collect(Collectors.toList());
            return new ResponseEntity<>(comDto, HttpStatus.OK);
        } else{
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

}
