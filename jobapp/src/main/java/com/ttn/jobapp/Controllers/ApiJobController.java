/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.JobDto;
import com.ttn.jobapp.Pojo.Account;
import com.ttn.jobapp.Pojo.Category;
import com.ttn.jobapp.Pojo.Company;
import com.ttn.jobapp.Pojo.Job;
import com.ttn.jobapp.Pojo.JobCandidate;
import com.ttn.jobapp.Services.AccountService;
import com.ttn.jobapp.Services.CategoryService;
import com.ttn.jobapp.Services.CompanyService;
import com.ttn.jobapp.Services.EmailService;
import com.ttn.jobapp.Services.JobCandidateService;
import com.ttn.jobapp.Services.JobService;
import com.ttn.jobapp.ServicesImpl.FirebaseService;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Win11
 */
@RestController
@CrossOrigin
@RequestMapping("api/job")
public class ApiJobController {

    @Autowired
    private JobService js;

    @Autowired
    private JobCandidateService jcs;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private AccountService as;

    @Autowired
    private CompanyService cs;

    @Autowired
    private CategoryService cateS;

    @GetMapping
    public ResponseEntity<List<Job>> allJobs() {
        return new ResponseEntity<>(this.js.getJobs(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobDto>> findingJobs(@RequestParam Map<String, String> params) {
        String keyword = params.get("keyword");
        Long cateId = null;
        String province = params.get("province");

        if (params.containsKey("categoryId") && params.get("categoryId") != null) {
            try {
                cateId = Long.valueOf(params.get("categoryId"));
            } catch (NumberFormatException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        List<JobDto> resultJobs;

        if (keyword == null && cateId == null && province == null) {
            resultJobs = this.js.getFindingJobs(null, null, null);
        } else {
            resultJobs = this.js.getFindingJobs(keyword != null ? keyword.toLowerCase() : null, cateId, province);
        }

        if (resultJobs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(resultJobs, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<JobDto> getJobDetail(@PathVariable("id") Long id) {
        Job job = this.js.getJobById(id);

        JobDto jDto = new JobDto();
        jDto.setId(job.getId());
        jDto.setDetail(job.getDetail());
        jDto.setAddress(job.getCompany().getAddress().getProvince());
        jDto.setCompanyLogo(job.getCompany().getLogo());
        jDto.setName(job.getName());
        jDto.setCompanyId(job.getCompany().getId());
        jDto.setCompanyName(job.getCompany().getName());
        jDto.setSalary(job.getSalary());
        jDto.setCategoryId(job.getCategory().getId());
        jDto.setCreatedDate(job.getCreatedDate());
        jDto.setEndDate(job.getEndDate());
        jDto.setExperience(job.getExperience());

        return new ResponseEntity<>(jDto, HttpStatus.OK);
    }

    @GetMapping("/get-job-by-company-id/{id}")
    public ResponseEntity<List<JobDto>> getJobByCompanyId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(this.js.getJobByCompany(id), HttpStatus.OK);
    }

    @GetMapping("/get-save-job-by-account-id/{id}")
    public ResponseEntity<List<JobDto>> getSavedJob(@PathVariable("id") Long id) {
        List<JobCandidate> jobCandidates = this.jcs.getJobsByCandidateId(id);

        List<JobDto> resultJobs = jobCandidates.stream()
                .map(x -> {
                    JobDto jDto = new JobDto();
                    jDto.setId(x.getId());
                    jDto.setDetail(x.getJob().getDetail());
                    jDto.setAddress(x.getJob().getCompany().getAddress().getProvince());
                    jDto.setCompanyLogo(x.getJob().getCompany().getLogo());
                    jDto.setName(x.getJob().getName());
                    jDto.setCompanyId(x.getJob().getCompany().getId());
                    jDto.setCompanyName(x.getJob().getCompany().getName());
                    jDto.setSalary(x.getJob().getSalary());
                    jDto.setCategoryId(x.getJob().getCategory().getId());
                    jDto.setCreatedDate(x.getJob().getCreatedDate());
                    jDto.setEndDate(x.getJob().getEndDate());
                    jDto.setExperience(x.getJob().getExperience());
                    return jDto;
                }).collect(Collectors.toList());

        return new ResponseEntity<>(resultJobs, HttpStatus.OK);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<JobDto>> getRecentJobs() {
        List<JobDto> recentJobs = js.getRecentJobs();
        return new ResponseEntity<>(recentJobs, HttpStatus.OK);
    }

    @GetMapping("/{jobId}/related-by-keyword")
    public ResponseEntity<List<JobDto>> getRelatedJobsByKeyword(@PathVariable Long jobId, @RequestParam String keyword) {
        Job currentJob = js.getJobById(jobId);
        if (currentJob == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<JobDto> relatedJobs = js.getRelatedJobsByKeyword(jobId, keyword);

        return new ResponseEntity<>(relatedJobs, HttpStatus.OK);
    }

    @PostMapping("/apply")
    public ResponseEntity<String> applyJob(
            @RequestParam("jobId") Long jobId,
            @RequestParam("accountId") Long accountId,
            @RequestParam("applicantName") String applicantName,
            @RequestParam("applicantEmail") String applicantEmail,
            @RequestParam("cvFile") MultipartFile cvFile) {

        Job job = js.getJobById(jobId);
        Account account = as.getAccountById(accountId);

        if (job == null) {
            return new ResponseEntity<>("Job not found.", HttpStatus.NOT_FOUND);
        }

        String companyEmail = job.getCompany().getEmail();
        String jobName = job.getName();
        String companyName = job.getCompany().getName();

        String subjectToCompany = "Ứng viên đã ứng tuyển vào vị trí: " + jobName;
        String messageToCompany = "Ứng viên " + applicantName + " (" + applicantEmail + ") đã ứng tuyển vào công việc: "
                + jobName + " tại công ty " + companyName + ". Vui lòng xem CV đính kèm.";

        JobCandidate jobCandidate = this.jcs.getJobCandidateByJobAndCandidate(accountId, jobId);

        if (jobCandidate == null) {
            jobCandidate = new JobCandidate();
            jobCandidate.setJob(job);
            jobCandidate.setAccount(account);
            jobCandidate.setApplied(Boolean.TRUE);
            jobCandidate.setSavedAt(LocalDateTime.now());
            this.jcs.save(jobCandidate);
        } else {
            jobCandidate.setApplied(Boolean.TRUE);
            jobCandidate.setSavedAt(LocalDateTime.now());
            this.jcs.save(jobCandidate);
        }

        try {
            emailService.sendEmailWithAttachment(
                    companyEmail,
                    "tonhanlk113@gmail.com",
                    subjectToCompany,
                    messageToCompany,
                    cvFile
            );

            String subjectToApplicant = "Xác nhận ứng tuyển thành công vào vị trí: " + jobName;
            String messageToApplicant = "Email ứng tuyển đã được gửi đến cho " + companyName + " tại vị trí " + jobName
                    + ", vui lòng chú ý email và điện thoại để có thể nhận được phản hồi từ " + companyName;
            emailService.sendSimpleEmail(applicantEmail, subjectToApplicant, messageToApplicant);

        } catch (MessagingException | IOException e) {
            return new ResponseEntity<>("Failed to send email.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        firebaseService.sendNotification(jobId, applicantName, applicantEmail);

        return new ResponseEntity<>("Applied successfully!", HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createJob(@RequestParam Map<String, String> params) {
        try {
            Long cateId = Optional.ofNullable(params.get("categoryId"))
                    .map(Long::valueOf)
                    .orElseThrow(() -> new IllegalArgumentException("categoryId is required"));
            Long accountId = Optional.ofNullable(params.get("accountId"))
                    .map(Long::valueOf)
                    .orElseThrow(() -> new IllegalArgumentException("accountId is required"));
            Long companyId = Optional.ofNullable(params.get("companyId"))
                    .map(Long::valueOf)
                    .orElseThrow(() -> new IllegalArgumentException("companyId is required"));

            String detail = params.getOrDefault("detail", "");
            String endDate = params.getOrDefault("endDate", "");
            String experience = params.getOrDefault("experience", "");
            String name = params.getOrDefault("name", "");
            String salary = params.getOrDefault("salary", "");

            Account account = this.as.getAccountById(accountId);
            Category category = this.cateS.getCateById(cateId);
            Company company = this.cs.getCompanyById(companyId);

            Job job = new Job();
            job.setAccount(account);
            job.setCategory(category);
            job.setCompany(company);
            job.setDetail(detail);
            job.setCreatedDate(LocalDate.now());
            job.setEndDate(LocalDate.parse(endDate));
            job.setExperience(experience);
            job.setName(name);
            job.setSalary(salary);
            job.setEnable(Boolean.FALSE);

            this.js.save(job);

            return new ResponseEntity<>("Created job successfully!", HttpStatus.CREATED);

        } catch (IllegalArgumentException | DateTimeParseException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/publish/{jobId}")
    public ResponseEntity<String> publishJob(@PathVariable("jobId") Long jobId) {
        Job job = js.getJobById(jobId);

        if (job == null) {
            return new ResponseEntity<>("Job not found!", HttpStatus.NOT_FOUND);
        }

        job.setEnable(Boolean.TRUE);
        this.js.save(job);

        return new ResponseEntity<>("Published job successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{jobId}")
    public ResponseEntity<String> deleteJob(@PathVariable("jobId") Long jobId) {
        Job job = js.getJobById(jobId);
        if (job == null) {
            return new ResponseEntity<>("Job not found!", HttpStatus.NOT_FOUND);
        }
        this.js.delete(jobId);
        return new ResponseEntity<>("Published job successfully!", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/get-applied-jobs/{id}")
    public ResponseEntity<List<JobDto>> getAppliedJobs(@PathVariable("id") Long id) {
        List<JobCandidate> appliedJobCandidate = this.jcs.getAppliedJobs(id);

        List<JobDto> resultJobs = appliedJobCandidate.stream()
                .map(x -> {
                    Job j = this.js.getJobById(x.getJob().getId());
                    JobDto jDto = new JobDto();
                    jDto.setId(j.getId());
                    jDto.setDetail(j.getDetail());
                    jDto.setAddress(j.getCompany().getAddress().getProvince());
                    jDto.setCompanyLogo(j.getCompany().getLogo());
                    jDto.setName(j.getName());
                    jDto.setCompanyId(j.getCompany().getId());
                    jDto.setCompanyName(j.getCompany().getName());
                    jDto.setSalary(j.getSalary());
                    jDto.setCategoryId(j.getCategory().getId());
                    jDto.setCreatedDate(j.getCreatedDate());
                    jDto.setEndDate(j.getEndDate());
                    jDto.setExperience(j.getExperience());
                    return jDto;
                }).collect(Collectors.toList());

        return new ResponseEntity<>(resultJobs, HttpStatus.OK);

    }

    @GetMapping("/get-saved-jobs/{id}")
    public ResponseEntity<List<JobDto>> getSavedJobs(@PathVariable("id") Long id) {
        List<JobCandidate> appliedJobCandidate = this.jcs.getSavedJobs(id);

        List<JobDto> resultJobs = appliedJobCandidate.stream()
                .map(x -> {
                    Job j = this.js.getJobById(x.getJob().getId());
                    JobDto jDto = new JobDto();
                    jDto.setId(j.getId());
                    jDto.setDetail(j.getDetail());
                    jDto.setAddress(j.getCompany().getAddress().getProvince());
                    jDto.setCompanyLogo(j.getCompany().getLogo());
                    jDto.setName(j.getName());
                    jDto.setCompanyId(j.getCompany().getId());
                    jDto.setCompanyName(j.getCompany().getName());
                    jDto.setSalary(j.getSalary());
                    jDto.setCategoryId(j.getCategory().getId());
                    jDto.setCreatedDate(j.getCreatedDate());
                    jDto.setEndDate(j.getEndDate());
                    jDto.setExperience(j.getExperience());
                    return jDto;
                }).collect(Collectors.toList());

        return new ResponseEntity<>(resultJobs, HttpStatus.OK);

    }

    @GetMapping("/get-jobs-by-company/search")
    public ResponseEntity<List<JobDto>> searchJobsInCompany(@RequestParam("keyword") String keyword, @RequestParam("companyId") Long companyId) {

        if (keyword == null || keyword.trim().isEmpty() || companyId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<JobDto> jobs = this.js.findCompanyJobsByKeyword(keyword.toLowerCase(), companyId);

        if (jobs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }

    @GetMapping("/get-jobs-by-category/categoryId")
    public ResponseEntity<List<JobDto>> searchJobsByCategory(@PathVariable("categoryId") Long categoryId) {

        if (categoryId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<JobDto> jobs = this.js.getJobByCategory(categoryId);

        if (jobs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }
}
