/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Pojo.CompanyCandidate;
import com.ttn.jobapp.Services.CompanyCandidateService;
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
@CrossOrigin
@RequestMapping("api/company-candidate")
@RestController
public class ApiCompanyCandidateController {

    @Autowired
    private CompanyCandidateService ccs;

    @GetMapping("/check-saved-status")
    public ResponseEntity<Boolean> checkSavedStatus(@RequestParam("candidateId") Long candidateId,
            @RequestParam("companyId") Long companyId) {
        return new ResponseEntity<>(this.ccs.checkSavedStatus(candidateId, companyId),
                HttpStatus.OK);
    }

    @PostMapping("/save-candidate/{candidateId}")
    public ResponseEntity<CompanyCandidate> saveCandidate(@PathVariable("candidateId") Long candidateId,
            @RequestParam("companyId") Long companyId) {
        return new ResponseEntity<>(this.ccs.saveCandidate(candidateId, companyId), HttpStatus.OK);
    }

    @PostMapping("/unsave-candidate/{candidateId}")
    public ResponseEntity<CompanyCandidate> unsaveCandidate(@PathVariable("candidateId") Long candidateId,
            @RequestParam("companyId") Long companyId) {
        return new ResponseEntity<>(this.ccs.unsaveCandidate(candidateId, companyId), HttpStatus.OK);
    }

}
