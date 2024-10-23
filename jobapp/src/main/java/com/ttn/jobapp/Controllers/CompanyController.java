/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ttn.jobapp.Dto.CompanyDto;
import com.ttn.jobapp.Pojo.Company;
import com.ttn.jobapp.Repositories.AddressRepository;
import com.ttn.jobapp.Repositories.CompanyRepository;
import com.ttn.jobapp.Repositories.RecruiterRepository;
import com.ttn.jobapp.Services.AddressService;
import com.ttn.jobapp.Services.CompanyService;
import com.ttn.jobapp.Utils.CloudinaryUtils;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Win11
 */
@Controller
@RequestMapping("admin/company")
public class CompanyController {

    @Autowired
    private CompanyService cs;

    @Autowired
    private CompanyRepository cr;

    @Autowired
    private AddressRepository ar;

    @Autowired
    private AddressService as;

    @Autowired
    private RecruiterRepository rr;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private CloudinaryUtils cloudinaryUtils;

    @GetMapping
    public String company(Model model) {
        model.addAttribute("companies", this.cs.getCompanies());
        return "admin/company/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        CompanyDto companyDto = new CompanyDto();
        model.addAttribute("companyDto", companyDto);
        model.addAttribute("addresses", this.as.getAddressNotAttach());
        model.addAttribute("recruiters", this.rr.findAll());
        return "admin/company/form";
    }

    @GetMapping("/delete")
    public String deleteCompany(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            this.cs.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Address deleted successfully!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete this address because it is associated with a company.");
        }
        return "redirect:/admin/company";
    }

    @PostMapping("/create")
    public String createCompany(@Valid @ModelAttribute CompanyDto companyDto, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "admin/company/form";
        }

        if (companyDto.getImageFile().isEmpty()) {
            result.addError(new FieldError("companyDto", "imageFile", "The image file is required"));
        }

        Company company = new Company();
        company.setEmail(companyDto.getEmail());
        company.setInformation(companyDto.getInformation());
        company.setName(companyDto.getName());
        company.setPhoneNumber(companyDto.getPhoneNumber());
        company.setWebsite(companyDto.getWebsite());
        company.setVerified(companyDto.getVerified());
        company.setAddress(this.ar.findById(companyDto.getAddressId()).get());
        company.setRecruiter(this.rr.findById(companyDto.getCreatedRecruiterId()).get());

        Map res = this.cloudinary.uploader().upload(companyDto.getImageFile().getBytes(), ObjectUtils.asMap("resource_type", "auto"));
        company.setLogo(res.get("secure_url").toString());

        this.cs.save(company);

        return "redirect:/admin/company";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam Long id) {
        try {
            Company company = cr.findById(id).get();
            model.addAttribute("company", company);
            model.addAttribute("addresses", this.as.getAddressNotAttach());

            CompanyDto companyDto = new CompanyDto();
            companyDto.setEmail(company.getEmail());
            companyDto.setInformation(company.getInformation());
            companyDto.setName(company.getName());
            companyDto.setPhoneNumber(company.getPhoneNumber());
            companyDto.setWebsite(company.getWebsite());
            companyDto.setVerified(company.isVerified());

            model.addAttribute("companyDto", companyDto);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "redirect:/admin/company";
        }

        return "admin/company/edit";
    }

    @PostMapping("/edit")
    public String updateCompany(Model model, @Valid @ModelAttribute CompanyDto companyDto,
            BindingResult result, @RequestParam Long id) throws IOException {

        Company company = cr.findById(id).get();

        model.addAttribute("company", company);

        String imageUrl = company.getLogo();

        if (result.hasErrors()) {
            return "admin/company/edit";
        }

        if (!companyDto.getImageFile().isEmpty()) {
            Map res = this.cloudinary.uploader().upload(companyDto.getImageFile().getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            company.setLogo(res.get("secure_url").toString());

            String publicId = cloudinaryUtils.extractPublicIdFromUrl(imageUrl);
            this.cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }

        if (companyDto.getAddressId() == null) {
            companyDto.setAddressId(company.getAddress().getId());
        } else {
            company.setAddress(this.ar.findById(companyDto.getAddressId()).get());
        }

        company.setEmail(companyDto.getEmail());
        company.setInformation(companyDto.getInformation());
        company.setName(companyDto.getName());
        company.setPhoneNumber(companyDto.getPhoneNumber());
        company.setWebsite(companyDto.getWebsite());
        company.setVerified(companyDto.getVerified());
        company.setRecruiter(this.rr.findById(companyDto.getCreatedRecruiterId()).get());

        this.cs.save(company);

        return "redirect:/admin/company";
    }
}
