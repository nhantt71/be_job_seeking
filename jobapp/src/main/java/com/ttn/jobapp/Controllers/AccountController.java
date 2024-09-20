/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ttn.jobapp.Dto.AccountDto;
import com.ttn.jobapp.Pojo.Account;
import com.ttn.jobapp.Repositories.AccountRepository;
import com.ttn.jobapp.Services.AccountService;
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
@RequestMapping("admin/account")
public class AccountController {

    @Autowired
    private AccountService as;

    @Autowired
    private AccountRepository ar;
    
    @Autowired
    private Cloudinary cloudinary;

    @GetMapping
    public String account(Model model) {
        model.addAttribute("accounts", this.as.getAccounts());
        return "admin/account/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        AccountDto accountDto = new AccountDto();
        accountDto.setRole("employee");
        model.addAttribute("accountDto", accountDto);
        return "admin/account/form";
    }

    @GetMapping("/delete")
    public String deleteAccount(@RequestParam("id") Long id) {
        this.as.delete(id);
        return "redirect:/admin/account";
    }

    @PostMapping("/create")
    public String createAccount(@Valid @ModelAttribute AccountDto accountDto, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return "admin/account/form";
        }

        if (accountDto.getImageFile().isEmpty()) {
            result.addError(new FieldError("accountDto", "imageFile", "The image file is required"));
        }

        Account account = new Account();
        account.setEmail(accountDto.getEmail());
        account.setPassword(accountDto.getPassword());
        account.setAvailable(Boolean.FALSE);
        account.setRole(accountDto.getRole());

        Map res = this.cloudinary.uploader().upload(accountDto.getImageFile().getBytes(), ObjectUtils.asMap("resource_type", "auto"));
        account.setAvatar(res.get("secure_url").toString());

        this.as.save(account);

        return "redirect:/admin/account";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam Long id) {
        try {
            Account account = ar.findById(id).get();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "redirect:/admin/account";
        }

        return "admin/account/edit";
    }

}
