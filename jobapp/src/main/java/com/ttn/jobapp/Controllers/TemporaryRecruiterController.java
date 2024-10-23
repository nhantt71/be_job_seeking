/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.TemporaryRecruiterDto;
import com.ttn.jobapp.Pojo.TemporaryRecruiter;
import com.ttn.jobapp.Repositories.TemporaryRecruiterRepository;
import com.ttn.jobapp.Services.CompanyService;
import com.ttn.jobapp.Services.RecruiterService;
import com.ttn.jobapp.Services.TemporaryRecruiterService;
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
@RequestMapping("admin/temporary-recruiter")
public class TemporaryRecruiterController {
    
    @Autowired
    private TemporaryRecruiterRepository trr;
    
    @Autowired
    private TemporaryRecruiterService trs;
    
    @Autowired
    private CompanyService comService;
    
    @Autowired
    private RecruiterService rs;
    
    @GetMapping
    public String temporaryRecruiter(Model model) {
        model.addAttribute("temporaryRecruiters", this.trs.getTempRecruiter());
        return "admin/temporaryrecruiter/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        TemporaryRecruiterDto temporaryRecruiterDto = new TemporaryRecruiterDto();
        model.addAttribute("temporaryRecruiterDto", temporaryRecruiterDto);
        model.addAttribute("recruiters", this.rs.getRecruitersWithoutCompanyId());
        model.addAttribute("companies", this.comService.getCompanies());
        return "admin/temporaryrecruiter/form";
    }

    @GetMapping("/delete")
    public String deleteTemporaryRecruiter(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            this.trs.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Address deleted successfully!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete this address because it is associated with a company.");
        }
        return "redirect:/admin/temporary-recruiter";
    }

    @PostMapping("/create")
    public String createTemporaryRecruiter(@Valid @ModelAttribute TemporaryRecruiterDto temporaryRecruiterDto, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "admin/temporaryrecruiter/form";
        }

        TemporaryRecruiter temporaryRecruiter = new TemporaryRecruiter();
        temporaryRecruiter.setRecruiter(this.rs.getRecruiterById(temporaryRecruiterDto.getRecruiterId()));
        temporaryRecruiter.setCompany(this.comService.getCompanyById(temporaryRecruiterDto.getCompanyId()));

        this.trs.save(temporaryRecruiter);

        return "redirect:/admin/temporary-recruiter";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam Long id) {
        try {
            TemporaryRecruiter temporaryRecruiter = trr.findById(id).get();
            model.addAttribute("temporaryRecruiter", temporaryRecruiter);

            TemporaryRecruiterDto temporaryRecruiterDto = new TemporaryRecruiterDto();

            model.addAttribute("temporaryRecruiterDto", temporaryRecruiterDto);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "redirect:/admin/temporary-recruiter";
        }

        return "admin/temporaryrecruiter/edit";
    }

    @PostMapping("/edit")
    public String updateCompanyCandidate(Model model, @Valid @ModelAttribute TemporaryRecruiterDto temporaryRecruiterDto,
            BindingResult result, @RequestParam Long id) {

        TemporaryRecruiter temporaryRecruiter = trr.findById(id).get();
        model.addAttribute("temporaryRecruiter", temporaryRecruiter);

        if (result.hasErrors()) {
            return "admin/temporaryrecruiter/edit";
        }

        this.trs.save(temporaryRecruiter);

        return "redirect:/admin/temporary-recruiter";
    }
    
}
