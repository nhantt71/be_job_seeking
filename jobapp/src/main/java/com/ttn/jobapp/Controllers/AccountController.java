/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.AccountDto;
import com.ttn.jobapp.Pojo.Account;
import com.ttn.jobapp.Repositories.AccountRepository;
import com.ttn.jobapp.Services.AccountService;
import com.ttn.jobapp.Services.SupabaseStorageService;
import com.ttn.jobapp.Utils.AuthProvider;
import com.ttn.jobapp.Utils.GenerateUniqueFileName;
import com.ttn.jobapp.Utils.Role;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping("admin/account")
@CrossOrigin
public class AccountController {

    @Autowired
    private AccountService as;

    @Autowired
    private AccountRepository ar;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SupabaseStorageService supabaseStorageService;

    @GetMapping
    public String account(Model model) {
        model.addAttribute("accounts", this.as.getAccounts());
        return "admin/account/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        AccountDto accountDto = new AccountDto();
        accountDto.setRole("candidate");
        accountDto.setVerified(false);
        model.addAttribute("accountDto", accountDto);
        return "admin/account/form";
    }

    @GetMapping("/delete")
    public String deleteAccount(@RequestParam("id") Long id) {
        this.as.delete(id);
        return "redirect:/admin/account";
    }


    @PostMapping("/create")
    public String createAccount(@Valid @ModelAttribute AccountDto accountDto, BindingResult result) throws IOException, Exception {
        if (result.hasErrors()) {
            return "admin/account/form";
        }

        if (accountDto.getImageFile().isEmpty()) {
            result.addError(new FieldError("accountDto", "imageFile", "The image file is required"));
        }

        Account account = new Account();
        account.setEmail(accountDto.getEmail());
        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        account.setVerified(accountDto.getVerified());
        account.setProvider(AuthProvider.LOCAL);

        account.setRole(Role.valueOf(accountDto.getRole().toUpperCase()));

        String avatarUrl = supabaseStorageService.uploadFile(
                "avatar",
                GenerateUniqueFileName.generateUniqueFileName(accountDto.getImageFile().getOriginalFilename()),
                accountDto.getImageFile().getInputStream(),
                accountDto.getImageFile().getContentType()
        );
        account.setAvatar(avatarUrl);

        this.as.save(account);

        return "redirect:/admin/account";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam Long id) {
        try {
            Account account = ar.findById(id).get();
            model.addAttribute("account", account);

            AccountDto accountDto = new AccountDto();
            accountDto.setEmail(account.getEmail());
            accountDto.setPassword(account.getPassword());
            accountDto.setRole(account.getRole().name());
            accountDto.setVerified(account.getVerified());

            model.addAttribute("accountDto", accountDto);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "redirect:/admin/account";
        }

        return "admin/account/edit";
    }

    @PostMapping("/edit")
    public String updateAccount(Model model, @Valid @ModelAttribute AccountDto accountDto,
            BindingResult result, @RequestParam Long id) throws IOException, Exception {

        Account account = ar.findById(id).get();
        model.addAttribute("account", account);
        if (result.hasErrors()) {
            return "admin/account/edit";
        }
        if (!accountDto.getImageFile().isEmpty()) {
            String avatarUrl = supabaseStorageService.uploadFile(
                    "avatar",
                    GenerateUniqueFileName.generateUniqueFileName(accountDto.getImageFile().getOriginalFilename()),
                    accountDto.getImageFile().getInputStream(),
                    accountDto.getImageFile().getContentType()
            );
            account.setAvatar(avatarUrl);
        }
        if (!account.getPassword().equals(accountDto.getPassword())) {
            account.setPassword(accountDto.getPassword());
        }
        account.setVerified(accountDto.getVerified());
        account.setEmail(accountDto.getEmail());
        account.setRole(Role.valueOf(accountDto.getRole().toUpperCase()));
        this.as.save(account);

        return "redirect:/admin/account";
    }
}
