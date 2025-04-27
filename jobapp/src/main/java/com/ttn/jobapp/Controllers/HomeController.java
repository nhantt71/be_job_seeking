/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Pojo.Account;
import com.ttn.jobapp.Services.AccountService;
import java.security.Principal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Win11
 */
@Controller
public class HomeController {

    @Autowired
    private AccountService accountService;

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/admin/profile")
    public String adminProfile(Model model, Principal principal) {
        Optional<Account> admin = accountService.findAdminByEmail(principal.getName());
        model.addAttribute("admin", admin.get());
        return "admin/profile/index";
    }

    @GetMapping("/admin/profile/update")
    public String showUpdateProfileForm(Model model, Principal principal) {
        Optional<Account> admin = accountService.findAdminByEmail(principal.getName());
        if (admin.isPresent()) {
            model.addAttribute("admin", admin.get());
            return "admin/profile/update/index";
        }
        return "redirect:/admin/profile";
    }

    @PostMapping("/admin/profile/update")
    public String updateProfile(@RequestParam String email,
            @RequestParam String avatar,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String confirmPassword,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        Optional<Account> optionalAdmin = accountService.findAdminByEmail(principal.getName());

        if (optionalAdmin.isPresent()) {
            Account admin = optionalAdmin.get();
            admin.setEmail(email);
            admin.setAvatar(avatar);

            if (newPassword != null && !newPassword.isBlank()) {
                if (!newPassword.equals(confirmPassword)) {
                    redirectAttributes.addFlashAttribute("error", "Passwords do not match!");
                    return "redirect:/admin/profile";
                }
                admin.setPassword(passwordEncoder.encode(newPassword));
            }

            accountService.save(admin);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        }

        return "redirect:/admin/profile";
    }
}
