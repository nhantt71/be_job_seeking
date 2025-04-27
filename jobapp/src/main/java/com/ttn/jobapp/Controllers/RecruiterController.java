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
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
        List<Recruiter> recruiters = this.rs.getRecruiters();
        if (recruiters == null) {
            System.out.println("Recruiters list is null!");
        } else {
            System.out.println("Recruiters list size: " + recruiters.size());
        }
        model.addAttribute("recruiters", recruiters);
        return "admin/recruiter/index";
    }

    @GetMapping("/delete")
    public String deleteRecruiter(@RequestParam("id") Long id) {
        this.cs.delete(this.cs.getCompanyByRecruiterId(id).getId());
        Recruiter r = rr.findById(id).get();
        as.delete(r.getAccount().getId());
        this.rs.delete(r.getId());
        return "redirect:/admin/recruiter";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        RecruiterDto recruiterDto = new RecruiterDto();
        List<Company> companies = cs.getCompanies();
        List<Account> accounts = as.getUnattachRecruiterAccounts();
        model.addAttribute("recruiterDto", recruiterDto);
        model.addAttribute("companies", companies);
        model.addAttribute("accounts", accounts);
        return "admin/recruiter/form";
    }

    @PostMapping("/create")
    public String createRecruiter(@Valid @ModelAttribute RecruiterDto recruiterDto, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "admin/recruiter/form";
        }

        Recruiter recruiter = new Recruiter();
        recruiter.setFullname(recruiterDto.getFullname());
        recruiter.setPhoneNumber(recruiterDto.getPhoneNumber());
        recruiter.setCity(recruiterDto.getCity());
        recruiter.setGender(recruiterDto.getGender());
        recruiter.setProvince(recruiterDto.getProvince());
        recruiter.setAccount(this.as.getAccountById(recruiterDto.getAccountId()));
        this.rs.save(recruiter);

        return "redirect:/admin/recruiter";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam Long id) {
        try {
            Recruiter recruiter = rr.findById(id).get();
            model.addAttribute("recruiter", recruiter);

            RecruiterDto recruiterDto = new RecruiterDto();
            recruiterDto.setFullname(recruiter.getFullname());
            recruiterDto.setPhoneNumber(recruiter.getPhoneNumber());
            recruiterDto.setCity(recruiter.getCity());
            recruiterDto.setGender(recruiter.getGender());
            recruiterDto.setProvince(recruiter.getProvince());

            model.addAttribute("recruiterDto", recruiterDto);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "redirect:/admin/recruiter";
        }

        return "admin/recruiter/edit";
    }

    @PostMapping("/edit")
    public String updateRecruiter(Model model, @Valid @ModelAttribute RecruiterDto recruiterDto,
            BindingResult result, @RequestParam Long id) {

        Recruiter recruiter = rr.findById(id).get();
        model.addAttribute("recruiter", recruiter);

        if (result.hasErrors()) {
            model.addAttribute("errorMessages", result.getAllErrors());
            return "admin/recruiter/edit";
        }

        recruiter.setFullname(recruiterDto.getFullname());
        recruiter.setPhoneNumber(recruiterDto.getPhoneNumber());
        recruiter.setCity(recruiterDto.getCity());
        recruiter.setGender(recruiterDto.getGender());
        recruiter.setProvince(recruiterDto.getProvince());

        this.rs.save(recruiter);

        return "redirect:/admin/recruiter";
    }

}
