/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Pojo.Account;
import com.ttn.jobapp.Services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Win11
 */
@Controller
@RequestMapping("admin/account")
public class AccountController {

    @Autowired
    private AccountService as;

    @GetMapping
    public String account(Model model) {
        model.addAttribute("accounts", this.as.getAccounts());
        return "admin/account/index";
    }

    @GetMapping("/create")
    public String createAccount(Model model) {
        model.addAttribute("account", new Account());
        return "admin/account/form";
    }

}
