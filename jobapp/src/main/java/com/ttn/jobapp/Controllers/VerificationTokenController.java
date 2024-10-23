/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.VerificationTokenDto;
import com.ttn.jobapp.Pojo.VerificationToken;
import com.ttn.jobapp.Repositories.VerificationTokenRepository;
import com.ttn.jobapp.Services.CompanyService;
import com.ttn.jobapp.Services.VerificationTokenService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
@RequestMapping("admin/verification-token")
public class VerificationTokenController {

    @Autowired
    private VerificationTokenService vts;

    @Autowired
    private CompanyService cs;

    @Autowired
    private VerificationTokenRepository vtr;

    @GetMapping
    public String verificationToken(Model model) {
        model.addAttribute("verificationTokens", this.vts.getVerificationTokens());
        return "admin/verificationtoken/index";
    }

    @GetMapping("/delete")
    public String deleteVerificationToken(@RequestParam("id") Long id) {
        this.vts.delete(id);
        return "redirect:/admin/verification-token";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        VerificationTokenDto verificationTokenDto = new VerificationTokenDto();
        verificationTokenDto.setToken(UUID.randomUUID().toString());
        verificationTokenDto.setExpiryDate(LocalDateTime.now().plusDays(3));
        model.addAttribute("verificationTokenDto", verificationTokenDto);
        model.addAttribute("companies", this.cs.getUnverifiedCompanies());
        return "admin/verificationtoken/form";
    }

    @PostMapping("/create")
    public String createverificationtoken(@Valid @ModelAttribute VerificationTokenDto verificationTokenDto, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return "admin/verificationtoken/form";
        }

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(verificationTokenDto.getToken());
        verificationToken.setExpiryDate(verificationTokenDto.getExpiryDate());
        verificationToken.setCompany(this.cs.getCompanyById(verificationTokenDto.getCompanyId()));

        this.vts.save(verificationToken);

        return "redirect:/admin/verification-token";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam Long id) {
        try {
            VerificationToken verificationToken = vtr.findById(id).get();
            model.addAttribute("verificationToken", verificationToken);

            VerificationTokenDto verificationTokenDto = new VerificationTokenDto();
            verificationTokenDto.setToken(verificationToken.getToken());
            verificationTokenDto.setExpiryDate(verificationToken.getExpiryDate());
            verificationTokenDto.setCompanyId(verificationToken.getCompany().getId());

            model.addAttribute("verificationTokenDto", verificationTokenDto);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "redirect:/admin/verification-token";
        }

        return "admin/verificationtoken/edit";
    }

    @PostMapping("/edit")
    public String updateVerificationToken(Model model, @Valid @ModelAttribute VerificationTokenDto verificationTokenDto,
            BindingResult result, @RequestParam Long id) {

        VerificationToken verificationToken = vtr.findById(id).orElse(null);
        if (verificationToken == null) {
            return "redirect:/admin/verification-token?error=notfound";
        }

        model.addAttribute("verificationToken", verificationToken);

        if (result.hasErrors()) {
            return "admin/verificationtoken/edit";
        }

        verificationToken.setExpiryDate(verificationTokenDto.getExpiryDate());

        this.vts.save(verificationToken);

        return "redirect:/admin/verification-token";
    }

}
