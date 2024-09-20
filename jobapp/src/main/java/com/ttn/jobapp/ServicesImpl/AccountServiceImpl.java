/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ttn.jobapp.Pojo.Account;
import com.ttn.jobapp.Repositories.AccountRepository;
import com.ttn.jobapp.Services.AccountService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Win11
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository ar;
    

    @Override
    public Account save(Account account) {
        return ar.save(account);
    }

    @Override
    public List<Account> getAccounts() {
        return ar.findAll();
    }

    @Override
    public Account update(Long id, Account account) {
        Account a = ar.findById(id).get();

        if (Objects.nonNull(a.getEmail()) && !account.getEmail().equalsIgnoreCase(a.getEmail())) {
            a.setEmail(account.getEmail());
        }
        if (Objects.nonNull(a.getAvatar()) && !account.getAvatar().equalsIgnoreCase(a.getAvatar())) {
            a.setAvatar(account.getAvatar());
        }
        if (Objects.nonNull(a.getPassword()) && !account.getPassword().equalsIgnoreCase(a.getPassword())) {
            a.setPassword(account.getPassword());
        }
        if (a.getAvailable() != null && !account.getAvailable().equals(a.getAvailable())) {
            a.setAvailable(!account.getAvailable());
        }
        return ar.save(a);
    }

    @Override
    public void delete(Long id) {
        Account a = ar.findById(id).get();
        ar.delete(a);
    }

}
