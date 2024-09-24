/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Pojo.Job;
import com.ttn.jobapp.Services.JobService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<List<Job>> allJobs(){
        return new ResponseEntity<>(this.js.getJobs(), HttpStatus.OK);
    }
}
