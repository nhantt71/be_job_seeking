/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Pojo.Account;
import com.ttn.jobapp.Pojo.Employee;
import com.ttn.jobapp.Pojo.Employer;
import com.ttn.jobapp.Repositories.AccountRepository;
import com.ttn.jobapp.Repositories.EmployeeRepository;
import com.ttn.jobapp.Repositories.EmployerRepository;
import com.ttn.jobapp.Services.AccountService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private EmployerRepository employerRepo;

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
        List<Employer> allEmployers = employerRepo.findAll();
        List<Employee> allEmployees = employeeRepo.findAll();
        List<Account> allAccounts = ar.findAll();

        List<Long> accountIds = new ArrayList<>();

        allEmployees.forEach(x -> {
            if (x.getAccount() != null) {
                accountIds.add(x.getAccount().getId());
            }
        });

        allEmployers.forEach(x -> {
            if (x.getAccount() != null) {
                accountIds.add(x.getAccount().getId());
            }
        });

        List<Account> unattachAccounts = allAccounts.stream()
                .filter(account -> !accountIds.contains(account.getId()))
                .collect(Collectors.toList());

        return unattachAccounts;
    }

}
