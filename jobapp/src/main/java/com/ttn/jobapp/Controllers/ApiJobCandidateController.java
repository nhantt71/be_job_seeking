/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.JobCandidateDto;
import com.ttn.jobapp.Pojo.Account;
import com.ttn.jobapp.Pojo.Job;
import com.ttn.jobapp.Pojo.JobCandidate;
import com.ttn.jobapp.Services.AccountService;
import com.ttn.jobapp.Services.JobCandidateService;
import com.ttn.jobapp.Services.JobService;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author MyLaptop
 */
@RestController
@RequestMapping("api/job-candidate")
@CrossOrigin
public class ApiJobCandidateController {

    @Autowired
    private JobCandidateService jcs;

    @Autowired
    private AccountService as;

    @Autowired
    private JobService js;

    @PostMapping("/save-job")
    public ResponseEntity<String> saveJob(@RequestParam Map<String, String> params) {

        Long jobId = null;
        Long candidateId = null;

        if (params.containsKey("jobId") && params.get("jobId") != null) {
            try {
                jobId = Long.valueOf(params.get("jobId"));
            } catch (NumberFormatException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        if (params.containsKey("candidateId") && params.get("candidateId") != null) {
            try {
                candidateId = Long.valueOf(params.get("candidateId"));
            } catch (NumberFormatException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        
        Job job = this.js.getJobById(jobId);
        Account candidate = this.as.getAccountById(candidateId);
                 
        JobCandidate jobCandidate = new JobCandidate();

        jobCandidate.setJob(job);
        jobCandidate.setAccount(candidate);
        jobCandidate.setSavedAt(LocalDateTime.now());

        this.jcs.save(jobCandidate);
        
        return new ResponseEntity<>("Saved job successfully!", HttpStatus.CREATED);
    }

}
