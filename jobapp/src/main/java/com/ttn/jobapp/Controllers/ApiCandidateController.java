/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.CandidateDto;
import com.ttn.jobapp.Pojo.Account;
import com.ttn.jobapp.Pojo.CV;
import com.ttn.jobapp.Pojo.Candidate;
import com.ttn.jobapp.Pojo.MongoExtractCV;
import com.ttn.jobapp.Repositories.MongoExtractCVRepository;
import com.ttn.jobapp.Services.AccountService;
import com.ttn.jobapp.Services.CVService;
import com.ttn.jobapp.Services.CandidateService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 *
 * @author Win11
 */
@RestController
@RequestMapping("api/candidate")
@CrossOrigin
public class ApiCandidateController {

    @Autowired
    private CandidateService cs;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AccountService as;

    @Autowired
    private CVService cvs;

    @GetMapping("/get-candidates")
    public ResponseEntity<List<Candidate>> getCandidates() {
        List<Candidate> candidates = this.cs.getCandidates();
        if (candidates.isEmpty()) {
            return new ResponseEntity<>(candidates, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(candidates, HttpStatus.OK);
        }
    }

    @GetMapping("/get-candidate/{id}")
    public ResponseEntity<CandidateDto> getCandidate(@PathVariable("id") Long id) {
        Candidate candidate = this.cs.getCandidateById(id);
        CandidateDto candidateDto = new CandidateDto();
        if (candidate == null) {
            return new ResponseEntity<>(candidateDto, HttpStatus.NOT_FOUND);
        } else {
            candidateDto.setAccountId(candidate.getAccount().getId());
            candidateDto.setFullname(candidate.getFullname());
            candidateDto.setPhoneNumber(candidate.getPhoneNumber());
            return new ResponseEntity<>(candidateDto, HttpStatus.OK);
        }
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<Candidate> editCandidate(@PathVariable("id") Long id, @RequestBody CandidateDto candidateDto) {
        Candidate candidate = this.cs.getCandidateById(id);

        if (candidate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            candidate.setFullname(candidateDto.getFullname());
            candidate.setPhoneNumber(candidateDto.getPhoneNumber());
            return new ResponseEntity<>(this.cs.save(candidate), HttpStatus.OK);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCandidate(
            @RequestParam Map<String, String> params,
            @RequestBody Account account) {

        if (!params.containsKey("phoneNumber") || params.get("phoneNumber").isEmpty()) {
            return new ResponseEntity<>("Phone number is required!", HttpStatus.BAD_REQUEST);
        }

        if (!params.containsKey("fullname") || params.get("fullname").isEmpty()) {
            return new ResponseEntity<>("Fullname is required!", HttpStatus.BAD_REQUEST);
        }

        if (account == null || account.getEmail() == null || account.getEmail().isEmpty()) {
            return new ResponseEntity<>("Account details are required!", HttpStatus.BAD_REQUEST);
        }

        Candidate candidate = new Candidate();
        candidate.setAccount(account);
        candidate.setFullname(params.get("fullname"));
        candidate.setPhoneNumber(params.get("phoneNumber"));

        try {
            this.cs.save(candidate);
            return new ResponseEntity<>("Created candidate successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while creating the candidate.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-candidate-by-email")
    public ResponseEntity<Candidate> getCandidateByEmail(@RequestParam("email") String email) {
        return new ResponseEntity<>(this.cs.getCandidateByEmail(email), HttpStatus.OK);
    }

    @PostMapping("/enable-finding-jobs/{id}")
    public ResponseEntity<Candidate> enableFindingJobs(@PathVariable("id") Long id) {
        Candidate candidate = cs.getCandidateById(id);

        candidate.setAvailable(Boolean.TRUE);

        return new ResponseEntity<>(this.cs.save(candidate), HttpStatus.OK);
    }

    @PostMapping("/disable-finding-jobs/{id}")
    public ResponseEntity<Candidate> disableFindingJobs(@PathVariable("id") Long id) {
        Candidate candidate = cs.getCandidateById(id);

        candidate.setAvailable(Boolean.FALSE);

        return new ResponseEntity<>(this.cs.save(candidate), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Long>> searchCVs(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String experience,
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String education,
            @RequestParam(required = false) String certification,
            @RequestParam(required = false) String goal) {

        Query query = new Query();

        if (gender != null && !gender.isEmpty()) {
            query.addCriteria(Criteria.where("gender").is(gender));
        }
        if (experience != null && !experience.isEmpty()) {
            query.addCriteria(Criteria.where("experience").regex(experience, "i"));
        }
        if (skill != null && !skill.isEmpty()) {
            query.addCriteria(Criteria.where("skill").regex(skill, "i"));
        }
        if (language != null && !language.isEmpty()) {
            query.addCriteria(Criteria.where("language").regex(language, "i"));
        }
        if (education != null && !education.isEmpty()) {
            query.addCriteria(Criteria.where("education").regex(education, "i"));
        }
        if (certification != null && !certification.isEmpty()) {
            query.addCriteria(Criteria.where("certification").regex(certification, "i"));
        }
        if (goal != null && !goal.isEmpty()) {
            query.addCriteria(Criteria.where("goal").regex(goal, "i"));
        }

        List<MongoExtractCV> cvs = mongoTemplate.find(query, MongoExtractCV.class);

        List<Long> candidateIds = cvs.stream()
                .map(MongoExtractCV::getCandidateId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new ResponseEntity<>(candidateIds, HttpStatus.OK);
    }

    @GetMapping("/get-available-candidates")
    public ResponseEntity<List<CandidateDto>> getAvailableCandidates() {
        List<Candidate> candidates = this.cs.getCandidates();

        List<Candidate> availableCandidates = candidates.stream()
                .filter(Candidate::getAvailable)
                .collect(Collectors.toList());

        List<CandidateDto> cands = new ArrayList<>();
        availableCandidates.forEach(x -> {
            CandidateDto cDto = new CandidateDto();
            cDto.setId(x.getId());
            cDto.setFullname(x.getFullname());
            cDto.setPhoneNumber(x.getPhoneNumber());

            Account a = this.as.getAccountById(x.getAccount().getId());
            cDto.setEmail(a.getEmail());

            String fileCV = this.cvs.getMainCVByCandidateId(x.getId());
            cDto.setFileCV(fileCV);

            cands.add(cDto);
        });

        return new ResponseEntity<>(cands, HttpStatus.OK);

    }

    @PostMapping("/get-search-candidates")
    public ResponseEntity<List<CandidateDto>> getSearchCandidates(@RequestBody List<Long> candidateIds) {
        List<Candidate> candidates = this.cs.getCandidatesByIds(candidateIds);

        List<CandidateDto> cands = new ArrayList<>();
        candidates.forEach(x -> {
            CandidateDto cDto = new CandidateDto();
            cDto.setId(x.getId());
            cDto.setFullname(x.getFullname());
            cDto.setPhoneNumber(x.getPhoneNumber());

            Account a = this.as.getAccountById(x.getAccount().getId());
            cDto.setEmail(a.getEmail());

            String fileCV = this.cvs.getMainCVByCandidateId(x.getId());
            cDto.setFileCV(fileCV);

            cands.add(cDto);
        });

        return new ResponseEntity<>(cands, HttpStatus.OK);
    }

}
