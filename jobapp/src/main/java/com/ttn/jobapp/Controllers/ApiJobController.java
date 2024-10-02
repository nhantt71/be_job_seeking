/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.JobDto;
import com.ttn.jobapp.Pojo.Job;
import com.ttn.jobapp.Pojo.JobCandidate;
import com.ttn.jobapp.Services.JobCandidateService;
import com.ttn.jobapp.Services.JobService;
import java.util.List;
import java.util.Map;
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
@CrossOrigin
@RequestMapping("api/job")
public class ApiJobController {

    @Autowired
    private JobService js;
    
    @Autowired
    private JobCandidateService jcs;

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
    public ResponseEntity<List<JobDto>> getSavedJob(@PathVariable("id") Long id){
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

}
