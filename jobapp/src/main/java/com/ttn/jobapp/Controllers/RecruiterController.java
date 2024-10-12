/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Services.CompanyService;
import com.ttn.jobapp.Dto.RecruiterDto;
import com.ttn.jobapp.Pojo.Account;
import com.ttn.jobapp.Pojo.Company;
import com.ttn.jobapp.Pojo.Recruiter;
import com.ttn.jobapp.Repositories.RecruiterRepository;
import com.ttn.jobapp.Services.AccountService;
import com.ttn.jobapp.Services.RecruiterService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("admin/recruiter")
public class RecruiterController {
    
    @Autowired
    private RecruiterService rs;
    
    @Autowired
    private RecruiterRepository rr;
    
    @Autowired
    private AccountService as;
    
    @Autowired
    private CompanyService cs;
    
    @GetMapping
    public String recruiter(Model model) {
        model.addAttribute("employers", this.rs.getRecruiters());
        return "admin/recruiter/index";
    }
    
    @GetMapping("/delete")
    public String deleteEmployer(@RequestParam("id") Long id) {
        Recruiter r = rr.findById(id).get();
        as.delete(r.getAccount().getId());
        r.setCompany(null);
        this.rs.delete(r.getId());
        return "redirect:/admin/recruiter";
    }
    
    @GetMapping("/create")
    public String showCreatePage(Model model) {
        RecruiterDto recruiterDto = new RecruiterDto();
        List<Company> companies = cs.getCompanies();
        List<Account> accounts = as.getUnattachAccounts();
        model.addAttribute("recruiterDto", recruiterDto);
        model.addAttribute("companies", companies);
        model.addAttribute("accounts", accounts);
        return "admin/recruiter/form";
    }
    
}
