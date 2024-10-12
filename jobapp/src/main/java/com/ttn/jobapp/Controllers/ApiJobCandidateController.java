/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Pojo.Candidate;
import com.ttn.jobapp.Pojo.Job;
import com.ttn.jobapp.Pojo.JobCandidate;
import com.ttn.jobapp.Services.CandidateService;
import com.ttn.jobapp.Services.JobCandidateService;
import com.ttn.jobapp.Services.JobService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    private CandidateService as;

    @Autowired
    private JobService js;

    @PostMapping("/save-job")
    public ResponseEntity<String> saveJob(@RequestParam Map<String, String> params) {

        Long jobId = parseLongParam(params.get("jobId"));
        Long candidateId = parseLongParam(params.get("candidateId"));

        if (jobId == null || candidateId == null) {
            return new ResponseEntity<>("Invalid jobId or candidateId", HttpStatus.BAD_REQUEST);
        }

        Job job = this.js.getJobById(jobId);
        Candidate candidate = this.as.getCandidateById(candidateId);

        JobCandidate jobCandidate = this.jcs.getJobCandidateByJobAndCandidate(candidateId, jobId);

        if (jobCandidate == null) {
            jobCandidate = new JobCandidate();
            jobCandidate.setJob(job);
            jobCandidate.setCandidate(candidate);
        }

        jobCandidate.setSaved(Boolean.TRUE);
        jobCandidate.setSavedAt(LocalDateTime.now());

        this.jcs.save(jobCandidate);

        return new ResponseEntity<>("Job saved successfully!", HttpStatus.CREATED);
    }

    @PostMapping("/unsave-job")
    public ResponseEntity<String> unsaveJob(@RequestParam Map<String, String> params) {

        Long jobId = parseLongParam(params.get("jobId"));
        Long candidateId = parseLongParam(params.get("candidateId"));

        if (jobId == null || candidateId == null) {
            return new ResponseEntity<>("Invalid jobId or candidateId", HttpStatus.BAD_REQUEST);
        }

        JobCandidate jobCandidate = this.jcs.getJobCandidateByJobAndCandidate(candidateId, jobId);

        if (jobCandidate == null) {
            return new ResponseEntity<>("No saved job found for this candidate", HttpStatus.NOT_FOUND);
        }

        jobCandidate.setSaved(Boolean.FALSE);
        jobCandidate.setSavedAt(LocalDateTime.now());

        this.jcs.save(jobCandidate);

        return new ResponseEntity<>("Job unsaved successfully!", HttpStatus.OK);
    }
    

    private Long parseLongParam(String param) {
        try {
            return param != null ? Long.valueOf(param) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    @GetMapping("/check-saved-job")
    public ResponseEntity<Boolean> checkSavedJob(@RequestParam("candidateId") Long candidateId,
            @RequestParam("jobId") Long jobId){
        JobCandidate jc = this.jcs.getJobCandidateByJobAndCandidate(candidateId, jobId);
        
        if (jc.getSaved()){
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        } else{
            return new ResponseEntity<>(Boolean.FALSE, HttpStatus.OK);
        }
    }
    

}
