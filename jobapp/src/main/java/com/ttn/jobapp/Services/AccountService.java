/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;

import com.ttn.jobapp.Pojo.Account;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.userdetails.User;


public interface AccountService{

    Account save(Account account);

    List<Account> getAccounts();

    void delete(Long id);
    
    List<Account> getUnattachAccounts();
    
    Account getAccountById(Long id);
    
    User getCurrentUser();
    
    Optional<Account> getAccountByEmail(String email);

    String getRoleByEmail(String email);
    
    Optional<Account> findByVerifyToken(String verifyToken);
    
    Boolean existsByEmail(String email);
    
    Boolean checkVerified(String email);
    
}
