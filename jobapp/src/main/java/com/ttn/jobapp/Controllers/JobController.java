/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.JobDto;
import com.ttn.jobapp.Pojo.Job;
import com.ttn.jobapp.Repositories.JobRepository;
import com.ttn.jobapp.Services.CategoryService;
import com.ttn.jobapp.Services.CompanyService;
import com.ttn.jobapp.Services.JobService;
import com.ttn.jobapp.Services.RecruiterService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
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
@RequestMapping("admin/job")
public class JobController {

    @Autowired
    private JobService js;

    @Autowired
    private JobRepository jr;

    @Autowired
    private CategoryService cs;

    @Autowired
    private CompanyService comService;

    @Autowired
    private RecruiterService rs;

    @GetMapping
    public String job(Model model) {
        model.addAttribute("jobs", this.js.getJobs());
        return "admin/job/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        JobDto jobDto = new JobDto();
        model.addAttribute("jobDto", jobDto);
        model.addAttribute("categories", this.cs.getCategories());
        model.addAttribute("recruiters", this.rs.getRecruiters());
        model.addAttribute("companies", this.comService.getCompanies());
        return "admin/job/form";
    }

    @GetMapping("/delete")
    public String deleteJob(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            this.js.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Job deleted successfully!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete this job");
        }
        return "redirect:/admin/job";
    }

    @PostMapping("/create")
    public String createjob(@Valid @ModelAttribute JobDto jobDto, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "admin/job/form";
        }

        Job job = new Job();
        job.setCreatedDate(LocalDate.now());
        job.setDetail(jobDto.getDetail());
        job.setName(jobDto.getName());
        job.setExperience(jobDto.getExperience());
        job.setEnable(jobDto.getEnable());
        job.setEndDate(jobDto.getEndDate());
        job.setSalary(jobDto.getSalary());
        job.setCategory(this.cs.getCateById(jobDto.getCategoryId()));
        job.setRecruiter(this.rs.getRecruiterById(jobDto.getRecruiterId()));
        job.setCompany(this.comService.getCompanyById(jobDto.getCompanyId()));

        this.js.save(job);

        return "redirect:/admin/job";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam Long id) {
        try {
            Job job = jr.findById(id).get();
            model.addAttribute("job", job);
            model.addAttribute("categories", this.cs.getCategories());

            JobDto jobDto = new JobDto();

            jobDto.setDetail(job.getDetail());
            jobDto.setName(job.getName());
            jobDto.setExperience(job.getExperience());
            jobDto.setEnable(job.getEnable());
            jobDto.setEndDate(job.getEndDate());
            jobDto.setSalary(job.getSalary());

            model.addAttribute("jobDto", jobDto);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "redirect:/admin/job";
        }

        return "admin/job/edit";
    }

    @PostMapping("/edit")
    public String updatejob(Model model, @Valid @ModelAttribute JobDto jobDto,
            BindingResult result, @RequestParam Long id) throws IOException {

        Job job = jr.findById(id).get();
        model.addAttribute("job", job);
        model.addAttribute("categories", this.cs.getCategories());

        if (result.hasErrors()) {
            return "admin/job/edit";
        }
        
        if (jobDto.getCategoryId() == null) {
            jobDto.setCategoryId(job.getCategory().getId());
        } else {
            job.setCategory(this.cs.getCateById(jobDto.getCategoryId()));
        }

        job.setDetail(jobDto.getDetail());
        job.setName(jobDto.getName());
        job.setExperience(jobDto.getExperience());
        job.setEnable(jobDto.getEnable());
        job.setEndDate(jobDto.getEndDate());
        job.setSalary(jobDto.getSalary());

        this.js.save(job);

        return "redirect:/admin/job";
    }

}
