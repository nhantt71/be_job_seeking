/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.RecruiterDto;
import com.ttn.jobapp.Dto.TemporaryRecruiterDto;
import com.ttn.jobapp.Pojo.Account;
import com.ttn.jobapp.Pojo.Company;
import com.ttn.jobapp.Pojo.Recruiter;
import com.ttn.jobapp.Pojo.TemporaryRecruiter;
import com.ttn.jobapp.Services.AccountService;
import com.ttn.jobapp.Services.CompanyService;
import com.ttn.jobapp.Services.RecruiterService;
import com.ttn.jobapp.Services.TemporaryRecruiterService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Win11
 */
@RestController
@CrossOrigin
@RequestMapping("api/recruiter")
public class ApiRecruiterController {

    @Autowired
    private RecruiterService rs;

    @Autowired
    private TemporaryRecruiterService tempReService;

    @Autowired
    private CompanyService comService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/get-recruiter/{id}")
    public ResponseEntity<RecruiterDto> getRecruiter(@PathVariable("id") Long id) {
        Recruiter recruiter = this.rs.getRecruiterById(id);
        RecruiterDto recruiterDto = new RecruiterDto();
        if (recruiter == null) {
            return new ResponseEntity<>(recruiterDto, HttpStatus.NOT_FOUND);
        } else {
            recruiterDto.setAccountId(recruiter.getAccount().getId());
            recruiterDto.setFullname(recruiter.getFullname());
            recruiterDto.setPhoneNumber(recruiter.getPhoneNumber());
            recruiterDto.setCity(recruiter.getCity());
            recruiterDto.setCompanyId(recruiter.getCompany().getId());
            recruiterDto.setGender(recruiter.getGender());
            recruiterDto.setProvince(recruiter.getProvince());
            return new ResponseEntity<>(recruiterDto, HttpStatus.OK);
        }
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<String> editRecruiter(@PathVariable("id") Long id, @RequestBody RecruiterDto emDto) {
        Recruiter recruiter = this.rs.getRecruiterById(id);

        if (recruiter == null) {
            return new ResponseEntity<>("Recruiter not found!", HttpStatus.NOT_FOUND);
        } else {
            recruiter.setFullname(emDto.getFullname());
            recruiter.setPhoneNumber(emDto.getPhoneNumber());
            recruiter.setCity(emDto.getCity());
            recruiter.setGender(emDto.getGender());
            recruiter.setProvince(emDto.getProvince());
            this.rs.save(recruiter);
            return new ResponseEntity<>("Edited recruiter successfully!", HttpStatus.OK);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRecruiterWithCompany(
            @RequestParam Map<String, String> params,
            @RequestBody Account account,
            @RequestBody Company company) {

        if (!params.containsKey("phoneNumber") || params.get("phoneNumber").isEmpty()) {
            return new ResponseEntity<>("Phone number is required!", HttpStatus.BAD_REQUEST);
        }

        if (!params.containsKey("fullname") || params.get("fullname").isEmpty()) {
            return new ResponseEntity<>("Fullname is required!", HttpStatus.BAD_REQUEST);
        }

        if (!params.containsKey("province") || params.get("province").isEmpty()) {
            return new ResponseEntity<>("Province is required!", HttpStatus.BAD_REQUEST);
        }

        if (!params.containsKey("city") || params.get("city").isEmpty()) {
            return new ResponseEntity<>("City is required!", HttpStatus.BAD_REQUEST);
        }

        if (!params.containsKey("gender") || params.get("gender").isEmpty()) {
            return new ResponseEntity<>("Gender is required!", HttpStatus.BAD_REQUEST);
        }

        if (account == null || account.getEmail() == null || account.getEmail().isEmpty()) {
            return new ResponseEntity<>("Account details are required!", HttpStatus.BAD_REQUEST);
        }

        Recruiter recruiter = new Recruiter();
        recruiter.setAccount(account);
        recruiter.setFullname(params.get("fullname"));
        recruiter.setPhoneNumber(params.get("phoneNumber"));
        recruiter.setCity(params.get("city"));
        recruiter.setProvince(params.get("province"));
        recruiter.setGender(params.get("gender"));
        recruiter.setCompany(company);
        
        Recruiter savedRecruiter = this.rs.save(recruiter);
        company.setRecruiter(savedRecruiter);
        this.comService.save(company);

        try {
            return new ResponseEntity<>(this.rs.save(savedRecruiter), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while creating the recruiter.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-without-company")
    public ResponseEntity<?> createRecruiterWithoutCompany(
            @RequestParam Map<String, String> params,
            @RequestBody Account account,
            @RequestBody Company company) {

        if (!params.containsKey("phoneNumber") || params.get("phoneNumber").isEmpty()) {
            return new ResponseEntity<>("Phone number is required!", HttpStatus.BAD_REQUEST);
        }

        if (!params.containsKey("fullname") || params.get("fullname").isEmpty()) {
            return new ResponseEntity<>("Fullname is required!", HttpStatus.BAD_REQUEST);
        }

        if (!params.containsKey("province") || params.get("province").isEmpty()) {
            return new ResponseEntity<>("Province is required!", HttpStatus.BAD_REQUEST);
        }

        if (!params.containsKey("city") || params.get("city").isEmpty()) {
            return new ResponseEntity<>("City is required!", HttpStatus.BAD_REQUEST);
        }

        if (!params.containsKey("gender") || params.get("gender").isEmpty()) {
            return new ResponseEntity<>("Gender is required!", HttpStatus.BAD_REQUEST);
        }

        if (account == null || account.getEmail() == null || account.getEmail().isEmpty()) {
            return new ResponseEntity<>("Account details are required!", HttpStatus.BAD_REQUEST);
        }

        Recruiter recruiter = new Recruiter();
        recruiter.setAccount(account);
        recruiter.setFullname(params.get("fullname"));
        recruiter.setPhoneNumber(params.get("phoneNumber"));
        recruiter.setCity(params.get("city"));
        recruiter.setProvince(params.get("province"));
        recruiter.setGender(params.get("gender"));

        Recruiter savedRecruiter = this.rs.save(recruiter);

        TemporaryRecruiter tempRecruiter = new TemporaryRecruiter();
        tempRecruiter.setRecruiter(savedRecruiter);
        tempRecruiter.setCompany(company);

        this.tempReService.save(tempRecruiter);

        try {
            return new ResponseEntity<>(savedRecruiter, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while creating the recruiter.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-recruiter-by-email")
    public ResponseEntity<RecruiterDto> getRecruiterByEmail(@RequestParam("email") String email) {
        Recruiter recruiter = this.rs.getRecruiterByEmail(email);
        RecruiterDto recruiterDto = new RecruiterDto();

        recruiterDto.setId(recruiter.getId());
        recruiterDto.setCity(recruiter.getCity());
        recruiterDto.setProvince(recruiter.getProvince());
        recruiterDto.setFullname(recruiter.getFullname());
        recruiterDto.setPhoneNumber(recruiter.getPhoneNumber());
        recruiterDto.setGender(recruiter.getGender());
        recruiterDto.setCompanyId(recruiter.getCompany().getId());
        recruiterDto.setAccountId(recruiter.getAccount().getId());

        return new ResponseEntity<>(recruiterDto, HttpStatus.OK);
    }

    @PostMapping("/kick-member/{recruiterId}")
    public ResponseEntity<String> kickMember(@PathVariable("recruiterId") Long recruiterId,
            @RequestParam("companyId") Long companyId) {
        Recruiter recruiter = this.rs.getRecruiterById(recruiterId);
        recruiter.setCompany(null);
        this.rs.save(recruiter);

        return new ResponseEntity<>("Kicked successfully", HttpStatus.OK);
    }

    @PostMapping("/approve-member/{recruiterId}")
    public ResponseEntity<Recruiter> approveMember(@PathVariable("recruiterId") Long recruiterId,
            @RequestParam("companyId") Long companyId) {
        Recruiter recruiter = this.rs.getRecruiterById(recruiterId);
        recruiter.setCompany(this.comService.getCompanyById(companyId));
        Recruiter savedRecruiter = this.rs.save(recruiter);
        TemporaryRecruiter tempRecruiter = this.tempReService.getTempRecruiterByIds(companyId, recruiterId);
        tempRecruiter.setCompany(null);
        this.tempReService.save(tempRecruiter);

        return new ResponseEntity<>(savedRecruiter, HttpStatus.OK);
    }

    @PostMapping("/decline-member/{recruiterId}")
    public ResponseEntity<String> declineMember(@PathVariable("recruiterId") Long recruiterId,
            @RequestParam("companyId") Long companyId) {
        TemporaryRecruiter tempRecruiter = this.tempReService.getTempRecruiterByIds(companyId, recruiterId);
        tempRecruiter.setCompany(null);
        this.tempReService.save(tempRecruiter);

        return new ResponseEntity<>("Declined successfully!", HttpStatus.OK);
    }

    @GetMapping("/get-waiting-recruiters-by-company/{companyId}")
    public ResponseEntity<List<TemporaryRecruiterDto>> getWaitingRecruitersByCompany(@PathVariable("companyId") Long companyId) {
        List<TemporaryRecruiter> tempRecruiters = this.tempReService.getTempRecruiterByCompany(companyId);

        List<TemporaryRecruiterDto> tempRecruiterDto = tempRecruiters.stream()
                .map(x -> {
                    TemporaryRecruiterDto temp = new TemporaryRecruiterDto();
                    temp.setCompanyId(companyId);
                    temp.setRecruiterId(x.getRecruiter().getId());
                    temp.setAvatar(x.getRecruiter().getAccount().getAvatar());
                    temp.setEmail(x.getRecruiter().getAccount().getEmail());
                    temp.setPhoneNumber(x.getRecruiter().getPhoneNumber());
                    temp.setFullname(x.getRecruiter().getFullname());
                    return temp;
                }).collect(Collectors.toList());

        return new ResponseEntity<>(tempRecruiterDto, HttpStatus.OK);
    }

    @GetMapping("/get-members-by-company/{companyId}")
    public ResponseEntity<List<RecruiterDto>> getRecruitersByCompany(@PathVariable("companyId") Long companyId) {
        List<Recruiter> recruiters = this.rs.getRecruitersByCompany(companyId);

        List<RecruiterDto> recruitersDto = recruiters.stream().map(x -> {
            RecruiterDto rDto = new RecruiterDto();
            rDto.setId(x.getId());
            rDto.setFullname(x.getFullname());
            rDto.setPhoneNumber(x.getPhoneNumber());
            rDto.setEmail(x.getAccount().getEmail());
            rDto.setAvatar(x.getAccount().getAvatar());
            return rDto;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(recruitersDto, HttpStatus.OK);
    }


}
