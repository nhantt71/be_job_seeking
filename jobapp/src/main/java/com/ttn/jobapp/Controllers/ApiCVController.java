/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ttn.jobapp.Pojo.CV;
import com.ttn.jobapp.Pojo.Candidate;
import com.ttn.jobapp.Services.CVService;
import com.ttn.jobapp.Services.CandidateService;
import java.io.IOException;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Win11
 */
@RestController
@RequestMapping("api/cv")
@CrossOrigin
public class ApiCVController {

    @Autowired
    private CVService cvService;

    @Autowired
    private CandidateService canService;

    @Autowired
    private Cloudinary cloudinary;

    @GetMapping("/get-cvs-by-candidate-id/{id}")
    public ResponseEntity<List<CV>> listCVCandidate(@PathVariable("id") Long id) {
        return new ResponseEntity<>(this.cvService.getCVsByCandidateId(id), HttpStatus.OK);
    }

    @PostMapping("/upload-cv")
    public ResponseEntity<CV> uploadCV(@RequestPart MultipartFile file,
            @RequestParam("candidateId") Long candidateId,
            @RequestParam("name") String name) {

        Candidate candidate = this.canService.getCandidateById(candidateId);

        CV cv = new CV();
        cv.setCandidate(candidate);
        cv.setName(name);
        cv.setUpdatedDate(LocalDate.now());

        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {

            Map<?, ?> res = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));

            String secureUrl = res.get("secure_url").toString();
            if (secureUrl.endsWith(".pdf")) {
                secureUrl = secureUrl.replace(".pdf", ".png");
            }

            cv.setFileCV(secureUrl);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(this.cvService.save(cv), HttpStatus.CREATED);
    }
    
    @PostMapping("/make-main-cv")
    public ResponseEntity<CV> makeMainCV(@RequestParam("id") Long id,
            @RequestParam("candidateId") Long candidateId){
        CV cv = this.cvService.getCVById(id);
        
        List<CV> listCV = this.cvService.getCVsByCandidateId(candidateId);
        
        listCV.forEach(x -> {
            x.setMainCV(Boolean.FALSE);
            this.cvService.save(x);
        });
        
        cv.setMainCV(Boolean.TRUE);
        
        return new ResponseEntity<>(this.cvService.save(cv), HttpStatus.OK);
    }
    
     @PostMapping("/unmake-all-main-cv")
    public ResponseEntity<String> unmakeAllMainCV(@RequestParam("candidateId") Long candidateId){
        List<CV> cvs = this.cvService.getCVsByCandidateId(candidateId);

        cvs.forEach(x -> {
            x.setMainCV(Boolean.FALSE);
        });
        
        return new ResponseEntity<>("Success false all main cv!", HttpStatus.OK);
    }
    
    @PostMapping("/unmake-main-cv")
    public ResponseEntity<CV> unmakeMainCV(@RequestParam("id") Long id){
        CV cv = this.cvService.getCVById(id);

        cv.setMainCV(Boolean.FALSE);
        
        return new ResponseEntity<>(this.cvService.save(cv), HttpStatus.OK);
    }
    
    @PostMapping("/delete-cv/{id}")
    public ResponseEntity<String> deleteCV(@PathVariable("id") Long id){
        this.cvService.delete(id);
        return new ResponseEntity<>("Deleted CV successfully!", HttpStatus.OK);
    }
    
    @GetMapping("/get-fileCV-by-CV-id/{id}")
    public ResponseEntity<String> getFileCVByCVId(@PathVariable("id") Long id){
        return new ResponseEntity<>(this.cvService.getCVById(id).getFileCV(), HttpStatus.OK);
    }
}
