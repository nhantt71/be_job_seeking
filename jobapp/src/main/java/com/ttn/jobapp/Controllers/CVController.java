/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ttn.jobapp.Dto.CVDto;
import com.ttn.jobapp.Pojo.CV;
import com.ttn.jobapp.Repositories.CVRepository;
import com.ttn.jobapp.Services.CVService;
import com.ttn.jobapp.Services.CandidateService;
import com.ttn.jobapp.Utils.CloudinaryUtils;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Win11
 */
@Controller
@RequestMapping("admin/cv")
public class CVController {

    @Autowired
    private CVService cs;

    @Autowired
    private CVRepository cr;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private CloudinaryUtils cloudinaryUtils;

    @GetMapping
    public String cv(Model model) {
        model.addAttribute("cvs", this.cs.getCVs());
        return "admin/cv/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        CVDto cvDto = new CVDto();
        model.addAttribute("cvDto", cvDto);
        model.addAttribute("candidates", this.candidateService.getCandidates());
        return "admin/cv/form";
    }

    @GetMapping("/delete")
    public String deleteCV(@RequestParam("id") Long id) {
        this.cs.delete(id);
        return "redirect:/admin/cv";
    }

    @PostMapping("/create")
    public String createCv(@Valid @ModelAttribute CVDto cvDto, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return "admin/cv/form";
        }

        if (cvDto.getImageFile().isEmpty()) {
            result.addError(new FieldError("cvDto", "imageFile", "The image file is required"));
        }

        CV cv = new CV();
        cv.setMainCV(cvDto.isMainCV());
        if (cvDto.isMainCV()) {
            this.cs.getCVsByCandidateId(cvDto.getCandidateId()).forEach(x -> {
                x.setMainCV(Boolean.FALSE);
                this.cs.save(x);
            });
        }
        cv.setName(cvDto.getName());
        cv.setUpdatedDate(cvDto.getUpdateDate());
        cv.setCandidate(this.candidateService.getCandidateById(cvDto.getCandidateId()));

        Map res = this.cloudinary.uploader().upload(cvDto.getImageFile().getBytes(), ObjectUtils.asMap("resource_type", "auto"));
        cv.setFileCV(res.get("secure_url").toString());

        this.cs.save(cv);

        return "redirect:/admin/cv";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam Long id) {
        try {
            CV cv = cr.findById(id).get();
            model.addAttribute("cv", cv);

            CVDto cvDto = new CVDto();
            cvDto.setMainCV(cv.getMainCV());
            cvDto.setUpdateDate(cv.getUpdatedDate());
            cvDto.setName(cv.getName());

            model.addAttribute("cvDto", cvDto);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "redirect:/admin/cv";
        }

        return "admin/cv/edit";
    }

    @PostMapping("/edit")
    public String updateCV(Model model, @Valid @ModelAttribute CVDto cvDto,
            BindingResult result, @RequestParam Long id) throws IOException {

        try {
            CV cv = cr.findById(id).get();
            model.addAttribute("cv", cv);

            String imageUrl = cv.getFileCV();

            if (result.hasErrors()) {
                return "admin/cv/edit";
            }

            if (!cvDto.getImageFile().isEmpty()) {
                Map res = this.cloudinary.uploader().upload(cvDto.getImageFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                cv.setFileCV(res.get("secure_url").toString());

                String publicId = cloudinaryUtils.extractPublicIdFromUrl(imageUrl);
                this.cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            }

            cv.setName(cvDto.getName());
            cv.setUpdatedDate(cvDto.getUpdateDate());
            cv.setMainCV(cvDto.isMainCV());

            if (cvDto.isMainCV()) {
                this.cs.getCVsByCandidateId(cvDto.getCandidateId()).forEach(x -> {
                    x.setMainCV(Boolean.FALSE);
                    this.cs.save(x);
                });
            }

            this.cs.save(cv);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return "redirect:/admin/cv";
    }
}
