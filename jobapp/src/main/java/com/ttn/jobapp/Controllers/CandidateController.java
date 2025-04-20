/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.CandidateDto;
import com.ttn.jobapp.Pojo.Account;
import com.ttn.jobapp.Pojo.Candidate;
import com.ttn.jobapp.Repositories.CandidateRepository;
import com.ttn.jobapp.Services.AccountService;
import com.ttn.jobapp.Services.CandidateService;
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
@RequestMapping("admin/candidate")
public class CandidateController {

    @Autowired
    private CandidateService cs;

    @Autowired
    private CandidateRepository cr;

    @Autowired
    private AccountService as;

    @GetMapping
    public String candidate(Model model) {
        model.addAttribute("candidates", this.cs.getCandidates());
        return "admin/candidate/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        CandidateDto candidateDto = new CandidateDto();
        model.addAttribute("candidateDto", candidateDto);
        model.addAttribute("accounts", this.as.getUnattachAccounts());
        return "admin/candidate/form";
    }

    @GetMapping("/delete")
    public String deleteCandidate(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Account account = this.as.getAccountById(this.cs.getCandidateById(id).getAccount().getId());
            this.as.delete(account.getId());
            this.cs.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Candidate deleted successfully!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Something's wrong!");
        }
        return "redirect:/admin/candidate";
    }

    @PostMapping("/create")
    public String createCandidate(@Valid @ModelAttribute CandidateDto candidateDto, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "admin/candidate/form";
        }

        Candidate candidate = new Candidate();
        candidate.setFullname(candidateDto.getFullname());
        candidate.setPhoneNumber(candidateDto.getPhoneNumber());
        candidate.setAvailable(candidateDto.getAvailable());
        candidate.setAccount(this.as.getAccountById(candidateDto.getAccountId()));
        

        this.cs.save(candidate);

        return "redirect:/admin/candidate";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam Long id) {
        try {
            Candidate candidate = cr.findById(id).get();
            model.addAttribute("candidate", candidate);

            CandidateDto candidateDto = new CandidateDto();
            candidateDto.setFullname(candidate.getFullname());
            candidateDto.setPhoneNumber(candidate.getPhoneNumber());
            candidateDto.setAvailable(candidate.getAvailable());

            model.addAttribute("candidateDto", candidateDto);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "redirect:/admin/candidate";
        }

        return "admin/candidate/edit";
    }

    @PostMapping("/edit")
    public String updateCandidate(Model model, @Valid @ModelAttribute CandidateDto candidateDto,
            BindingResult result, @RequestParam Long id) {

        Candidate candidate = cr.findById(id).get();
        model.addAttribute("candidate", candidate);
        
        if (result.hasErrors()) {
            return "admin/candidate/edit";
        }
        candidate.setAvailable(candidateDto.getAvailable());
        candidate.setFullname(candidateDto.getFullname());
        candidate.setPhoneNumber(candidateDto.getPhoneNumber());
        
        
        this.cs.save(candidate);

        return "redirect:/admin/candidate";
    }
    
}
