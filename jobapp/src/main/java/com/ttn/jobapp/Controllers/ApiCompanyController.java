/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ttn.jobapp.Dto.CompanyDto;
import com.ttn.jobapp.Pojo.Address;
import com.ttn.jobapp.Pojo.Company;
import com.ttn.jobapp.Pojo.Recruiter;
import com.ttn.jobapp.Pojo.VerificationToken;
import com.ttn.jobapp.Repositories.VerificationTokenRepository;
import com.ttn.jobapp.Services.AddressService;
import com.ttn.jobapp.Services.CompanyService;
import com.ttn.jobapp.Services.RecruiterService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("api/company")
@CrossOrigin
public class ApiCompanyController {

    @Autowired
    private CompanyService cs;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RecruiterService rs;

    @Autowired
    private AddressService as;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @GetMapping
    public ResponseEntity<List<CompanyDto>> allCompanies() {
        List<Company> companies = this.cs.getCompanies();

        List<CompanyDto> comDto = new ArrayList<>();

        companies.forEach(x -> {
            if (x.isVerified()) {
                CompanyDto c = new CompanyDto();

                c.setId(x.getId());
                c.setName(x.getName());
                c.setLogo(x.getLogo());
                c.setEmail(x.getEmail());
                c.setInformation(x.getInformation());
                c.setPhoneNumber(x.getPhoneNumber());
                c.setWebsite(x.getWebsite());
                c.setAddressDetail(x.getAddress().getDetail());
                c.setCity(x.getAddress().getCity());
                c.setProvince(x.getAddress().getProvince());
                c.setJobAmount(this.cs.jobAmount(x));

                comDto.add(c);
            }
        });

        return new ResponseEntity<>(comDto, HttpStatus.OK);
    }

    @GetMapping("/get-company-by-id/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable("id") Long id) {
        CompanyDto comDto = new CompanyDto();

        Company com = this.cs.getCompanyById(id);

        comDto.setId(com.getId());
        comDto.setEmail(com.getEmail());
        comDto.setInformation(com.getInformation());
        comDto.setLogo(com.getLogo());
        comDto.setName(com.getName());
        comDto.setPhoneNumber(com.getPhoneNumber());
        comDto.setWebsite(com.getWebsite());
        comDto.setAddressDetail(com.getAddress().getDetail());
        comDto.setCity(com.getAddress().getCity());
        comDto.setProvince(com.getAddress().getProvince());
        comDto.setCreatedRecruiterId(com.getRecruiter().getId());

        return new ResponseEntity<>(comDto, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CompanyDto>> getSearchCompanies(@RequestParam("keyword") String keyword) {
        String keyword1 = keyword;

        if (keyword1 != null) {
            List<Company> companies = this.cs.getFindingCompanies(keyword1.toLowerCase());

            List<CompanyDto> comDto = companies.stream()
                    .map(x -> {
                        CompanyDto c = new CompanyDto();
                        c.setId(x.getId());
                        c.setName(x.getName());
                        c.setLogo(x.getLogo());
                        c.setEmail(x.getEmail());
                        c.setInformation(x.getInformation());
                        c.setPhoneNumber(x.getPhoneNumber());
                        c.setWebsite(x.getWebsite());
                        c.setAddressDetail(x.getAddress().getDetail());
                        c.setCity(x.getAddress().getCity());
                        c.setProvince(x.getAddress().getProvince());
                        c.setJobAmount(this.cs.jobAmount(x));
                        return c;
                    }).collect(Collectors.toList());
            return new ResponseEntity<>(comDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCompany(
            @RequestParam Map<String, String> params,
            @RequestPart MultipartFile file,
            @RequestBody Recruiter recruiter,
            @RequestBody Address address) {

        if (!params.containsKey("email") || params.get("email").isEmpty()) {
            return new ResponseEntity<>("Email is required!", HttpStatus.BAD_REQUEST);
        }

        if (!params.containsKey("information") || params.get("information").isEmpty()) {
            return new ResponseEntity<>("Information is required!", HttpStatus.BAD_REQUEST);
        }

        if (!params.containsKey("name") || params.get("name").isEmpty()) {
            return new ResponseEntity<>("Name is required!", HttpStatus.BAD_REQUEST);
        }

        if (!params.containsKey("website") || params.get("website").isEmpty()) {
            return new ResponseEntity<>("Website is required!", HttpStatus.BAD_REQUEST);
        }

        if (!params.containsKey("phoneNumber") || params.get("phoneNumber").isEmpty()) {
            return new ResponseEntity<>("Phone number is required!", HttpStatus.BAD_REQUEST);
        }

        if (address == null) {
            return new ResponseEntity<>("Address are required!", HttpStatus.BAD_REQUEST);
        }

        if (file.isEmpty()) {
            return new ResponseEntity<>("Image file is required!", HttpStatus.BAD_REQUEST);
        }

        Company company = new Company();
        company.setAddress(address);
        company.setEmail(params.get("email"));
        company.setInformation(params.get("information"));
        company.setName(params.get("name"));
        company.setPhoneNumber(params.get("phoneNumber"));
        company.setWebsite(params.get("website"));
        company.setRecruiter(recruiter);
        company.setVerified(false);
        Long id = Long.valueOf("1");
        company.setRecruiter(this.rs.getRecruiterById(id));

        try {
            Map<?, ?> res = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
            company.setLogo(res.get("secure_url").toString());
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload image.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Company savedCompany = this.cs.save(company);

        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setCompany(savedCompany);
        verificationTokenRepository.save(verificationToken);

        sendVerificationEmail(params.get("email"), token);

        try {
            return new ResponseEntity<>(savedCompany, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while creating the candidate.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void sendVerificationEmail(String email, String token) {
        String subject = "Verify your company registration";
        String verificationUrl = "http://localhost:8080/api/company/verify?token=" + token;
        String message = "Please click the link below to verify your company registration:\n" + verificationUrl;

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(message, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyCompany(@RequestParam("token") String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            return new ResponseEntity<>("Invalid token", HttpStatus.BAD_REQUEST);
        }

        Company company = verificationToken.getCompany();
        company.setVerified(true);
        Recruiter recruiter = company.getRecruiter();
        recruiter.setCompany(company);
        rs.save(recruiter);
        cs.save(company);

        verificationTokenRepository.delete(verificationToken);

        return new ResponseEntity<>("Company successfully verified!", HttpStatus.OK);
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<CompanyDto> editCompany(@RequestParam Map<String, String> params,
            @PathVariable("id") Long id) {

        Company com = this.cs.getCompanyById(id);
        if (com == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (params.get("name") != null) {
            com.setName(params.get("name"));
        }
        if (params.get("email") != null) {
            com.setEmail(params.get("email"));
        }
        if (params.get("information") != null) {
            com.setInformation(params.get("information"));
        }
        if (params.get("phoneNumber") != null) {
            com.setPhoneNumber(params.get("phoneNumber"));
        }
        if (params.get("website") != null) {
            com.setWebsite(params.get("website"));
        }

        Address address = com.getAddress();
        if (address != null) {
            if (params.get("city") != null) {
                address.setCity(params.get("city"));
            }
            if (params.get("detail") != null) {
                address.setDetail(params.get("detail"));
            }
            if (params.get("province") != null) {
                address.setProvince(params.get("province"));
            }
        }

        Company savedCompany = cs.save(com);

        CompanyDto comDto = new CompanyDto();
        comDto.setId(savedCompany.getId());
        comDto.setEmail(savedCompany.getEmail());
        comDto.setInformation(savedCompany.getInformation());
        comDto.setLogo(savedCompany.getLogo());
        comDto.setName(savedCompany.getName());
        comDto.setPhoneNumber(savedCompany.getPhoneNumber());
        comDto.setWebsite(savedCompany.getWebsite());

        if (savedCompany.getAddress() != null) {
            comDto.setAddressDetail(savedCompany.getAddress().getDetail());
            comDto.setCity(savedCompany.getAddress().getCity());
            comDto.setProvince(savedCompany.getAddress().getProvince());
        }

        if (savedCompany.getRecruiter() != null) {
            comDto.setCreatedRecruiterId(savedCompany.getRecruiter().getId());
        }

        return new ResponseEntity<>(comDto, HttpStatus.OK);
    }

    @PostMapping("/change-logo")
    public ResponseEntity<Company> changeLogo(@RequestParam("id") Long id,
            @RequestPart MultipartFile file) {
        Company company = this.cs.getCompanyById(id);

        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Map<?, ?> res = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
            company.setLogo(res.get("secure_url").toString());
            return new ResponseEntity<>(this.cs.save(company), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
