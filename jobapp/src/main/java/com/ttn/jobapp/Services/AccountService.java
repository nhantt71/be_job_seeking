/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;

import com.ttn.jobapp.Pojo.Account;
import java.util.List;

/**
 *
 * @author Win11
 */
public interface AccountService {
    Account save(Account account);
    
    List<Account> getAccounts();
    
    Account update(Long id, Account account);
    
    void delete(Long id);
}
