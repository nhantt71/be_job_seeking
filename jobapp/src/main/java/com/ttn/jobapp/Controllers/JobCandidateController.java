/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.JobCandidateDto;
import com.ttn.jobapp.Pojo.JobCandidate;
import com.ttn.jobapp.Repositories.JobCandidateRepository;
import com.ttn.jobapp.Services.CandidateService;
import com.ttn.jobapp.Services.JobCandidateService;
import com.ttn.jobapp.Services.JobService;
import jakarta.validation.Valid;
import java.time.format.DateTimeFormatter;
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
@RequestMapping("admin/job-candidate")
public class JobCandidateController {

    @Autowired
    private JobCandidateRepository jcr;

    @Autowired
    private JobCandidateService jcs;

    @Autowired
    private CandidateService cs;

    @Autowired
    private JobService js;

    @GetMapping
    public String jobCandidate(Model model) {
        model.addAttribute("jobCandidates", this.jcs.getJobCandidates());
        return "admin/jobcandidate/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        JobCandidateDto jobCandidateDto = new JobCandidateDto();
        model.addAttribute("jobCandidateDto", jobCandidateDto);
        model.addAttribute("candidates", this.cs.getUnattachCandidates());
        model.addAttribute("jobs", this.js.getJobs());
        return "admin/jobcandidate/form";
    }

    @GetMapping("/delete")
    public String deleteCandidate(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            this.jcs.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Address deleted successfully!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete this address because it is associated with a company.");
        }
        return "redirect:/admin/job-candidate";
    }

    @PostMapping("/create")
    public String createJobCandidate(@Valid @ModelAttribute JobCandidateDto jobCandidateDto, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "admin/jobcandidate/form";
        }

        JobCandidate jobCandidate = new JobCandidate();
        jobCandidate.setApplied(jobCandidateDto.getApplied());
        jobCandidate.setSaved(jobCandidateDto.getSaved());
        jobCandidate.setAppliedAt(jobCandidateDto.getAppliedAt());
        jobCandidate.setSavedAt(jobCandidateDto.getSavedAt());
        jobCandidate.setCandidate(this.cs.getCandidateById(jobCandidateDto.getCandidateId()));
        jobCandidate.setJob(this.js.getJobById(jobCandidateDto.getJobId()));

        this.jcs.save(jobCandidate);

        return "redirect:/admin/job-candidate";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam Long id) {
        try {
            JobCandidate jobCandidate = jcr.findById(id).get();
            model.addAttribute("jobCandidate", jobCandidate);

            JobCandidateDto jobCandidateDto = new JobCandidateDto();
            jobCandidateDto.setApplied(jobCandidate.getApplied());
            jobCandidateDto.setSaved(jobCandidate.getSaved());
            jobCandidateDto.setAppliedAt(jobCandidate.getAppliedAt());
            jobCandidateDto.setSavedAt(jobCandidate.getSavedAt());

            model.addAttribute("jobCandidateDto", jobCandidateDto);
            
            model.addAttribute("formattedAppliedAt", jobCandidate.getAppliedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            model.addAttribute("formattedSavedAt", jobCandidate.getSavedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "redirect:/admin/job-candidate";
        }

        return "admin/jobcandidate/edit";
    }

    @PostMapping("/edit")
    public String updateJobCandidate(Model model, @Valid @ModelAttribute JobCandidateDto jobCandidateDto,
            BindingResult result, @RequestParam Long id) {

        JobCandidate jobCandidate = jcr.findById(id).get();
        model.addAttribute("jobcandidate", jobCandidate);

        if (result.hasErrors()) {
            return "admin/jobcandidate/edit";
        }

        jobCandidate.setApplied(jobCandidateDto.getApplied());
        jobCandidate.setSaved(jobCandidateDto.getSaved());
        jobCandidate.setAppliedAt(jobCandidateDto.getAppliedAt());
        jobCandidate.setSavedAt(jobCandidateDto.getSavedAt());

        this.jcs.save(jobCandidate);

        return "redirect:/admin/job-candidate";
    }
}
