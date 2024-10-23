/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.CompanyCandidateDto;
import com.ttn.jobapp.Pojo.CompanyCandidate;
import com.ttn.jobapp.Repositories.CompanyCandidateRepository;
import com.ttn.jobapp.Services.CandidateService;
import com.ttn.jobapp.Services.CompanyCandidateService;
import com.ttn.jobapp.Services.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Win11
 */
@Controller
@RequestMapping("admin/company-candidate")
public class CompanyCandidateController {
    
    @Autowired
    private CompanyCandidateService ccs;
    
    @Autowired
    private CompanyCandidateRepository ccr;
    
    @Autowired
    private CandidateService cs;
    
    @Autowired
    private CompanyService comService;
    
    
    @GetMapping
    public String companyCandidate(Model model) {
        model.addAttribute("companyCandidates", this.ccs.getCompanyCandidates());
        return "admin/companycandidate/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        CompanyCandidateDto companyCandidateDto = new CompanyCandidateDto();
        model.addAttribute("companyCandidateDto", companyCandidateDto);
        model.addAttribute("candidates", this.cs.getCandidatesWithoutCompany());
        model.addAttribute("companies", this.comService.getCompanies());
        return "admin/companycandidate/form";
    }

    @GetMapping("/delete")
    public String deleteCompanyCandidate(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            this.ccs.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Address deleted successfully!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete this address because it is associated with a company.");
        }
        return "redirect:/admin/company-candidate";
    }

    @PostMapping("/create")
    public String createCompanyCandidate(@Valid @ModelAttribute CompanyCandidateDto companyCandidateDto, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "admin/companycandidate/form";
        }

        CompanyCandidate companyCandidate = new CompanyCandidate();
        companyCandidate.setSaved(companyCandidateDto.getSaved());
        companyCandidate.setCandidate(this.cs.getCandidateById(companyCandidateDto.getCandidateId()));
        companyCandidate.setCompany(this.comService.getCompanyById(companyCandidateDto.getCompanyId()));

        this.ccs.save(companyCandidate);

        return "redirect:/admin/company-candidate";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam Long id) {
        try {
            CompanyCandidate companyCandidate = ccr.findById(id).get();
            model.addAttribute("companyCandidate", companyCandidate);

            CompanyCandidateDto companyCandidateDto = new CompanyCandidateDto();
            companyCandidateDto.setSaved(companyCandidate.getSaved());

            model.addAttribute("companyCandidateDto", companyCandidateDto);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "redirect:/admin/company-candidate";
        }

        return "admin/companycandidate/edit";
    }

    @PostMapping("/edit")
    public String updateCompanyCandidate(Model model, @Valid @ModelAttribute CompanyCandidateDto companyCandidateDto,
            BindingResult result, @RequestParam Long id) {

        CompanyCandidate companyCandidate = ccr.findById(id).get();
        model.addAttribute("companyCandidate", companyCandidate);

        if (result.hasErrors()) {
            return "admin/companycandidate/edit";
        }

        companyCandidate.setSaved(companyCandidateDto.getSaved());

        this.ccs.save(companyCandidate);

        return "redirect:/admin/company-candidate";
    }
}
