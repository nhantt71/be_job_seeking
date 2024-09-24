/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Services.CompanyService;
import com.ttn.jobapp.Dto.EmployerDto;
import com.ttn.jobapp.Pojo.Account;
import com.ttn.jobapp.Pojo.Company;
import com.ttn.jobapp.Pojo.Employer;
import com.ttn.jobapp.Repositories.EmployerRepository;
import com.ttn.jobapp.Services.AccountService;
import com.ttn.jobapp.Services.EmployerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("admin/employer")
public class EmployerController {
    
    @Autowired
    private EmployerService es;
    
    @Autowired
    private EmployerRepository er;
    
    @Autowired
    private AccountService as;
    
    @Autowired
    private CompanyService cs;
    
    @GetMapping
    public String employer(Model model) {
        model.addAttribute("employers", this.es.getEmployers());
        return "admin/employer/index";
    }
    
    @GetMapping("/delete")
    public String deleteEmployer(@RequestParam("id") Long id) {
        Employer e = er.findById(id).get();
        as.delete(e.getAccount().getId());
        e.setCompany(null);
        this.es.delete(e.getId());
        return "redirect:/admin/employer";
    }
    
    @GetMapping("/create")
    public String showCreatePage(Model model) {
        EmployerDto employerDto = new EmployerDto();
        List<Company> companies = cs.getCompanies();
        List<Account> accounts = as.getUnattachAccounts();
        model.addAttribute("employerDto", employerDto);
        model.addAttribute("companies", companies);
        model.addAttribute("accounts", accounts);
        return "admin/employer/form";
    }
    
}
