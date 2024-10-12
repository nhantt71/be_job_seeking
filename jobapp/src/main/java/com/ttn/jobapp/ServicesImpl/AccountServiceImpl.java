/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Configs.CustomUserDetailsService;
import com.ttn.jobapp.Pojo.*;
import com.ttn.jobapp.Repositories.*;
import com.ttn.jobapp.Services.AccountService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

/**
 *
 * @author Win11
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository ar;

    @Autowired
    private CandidateRepository candidateRepo;

    @Autowired
    private RecruiterRepository recruiterRepo;
    
    @Autowired
    private CustomUserDetailsService cuds;

    @Override
    public Account save(Account account) {
        return ar.save(account);
    }

    @Override
    public List<Account> getAccounts() {
        return ar.findAll();
    }

    @Override
    public void delete(Long id) {
        ar.deleteById(id);
    }

    @Override
    public List<Account> getUnattachAccounts() {
        List<Recruiter> allRecruiters = recruiterRepo.findAll();
        List<Candidate> allCandidates = candidateRepo.findAll();
        List<Account> allAccounts = ar.findAll();

        List<Long> accountIds = new ArrayList<>();

        allCandidates.forEach(x -> {
            if (x.getAccount() != null) {
                accountIds.add(x.getAccount().getId());
            }
        });

        allRecruiters.forEach(x -> {
            if (x.getAccount() != null) {
                accountIds.add(x.getAccount().getId());
            }
        });

        List<Account> unattachAccounts = allAccounts.stream()
                .filter(account -> !accountIds.contains(account.getId()))
                .collect(Collectors.toList());

        return unattachAccounts;
    }

    @Override
    public Account getAccountById(Long id) {
        return ar.findById(id).get();
    }
    
    

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()){
            String email = authentication.getName();
            return (User) cuds.loadUserByUsername(email);
        }
        return null;
    }

    @Override
    public Account getAccountByEmail(String email) {
        return ar.findByEmail(email);
    }

    @Override
    public String getRoleByEmail(String email) {
        return ar.getRoleByEmail(email);
    }


}
