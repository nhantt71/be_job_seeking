/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.JobDto;
import com.ttn.jobapp.Pojo.Job;
import com.ttn.jobapp.Services.JobService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
    
       
    @GetMapping("/detail")
    public ResponseEntity<JobDto> getJobDetail(Long id){
        Job job = this.js.getJobById(id);
        
        JobDto resultJob = new JobDto();
        resultJob.setAddress(job.getCompany().getAddress().getProvince());
        
        return new ResponseEntity<>(resultJob, HttpStatus.OK);
    } 

}
