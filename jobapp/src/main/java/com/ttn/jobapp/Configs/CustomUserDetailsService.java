/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Configs;

import com.ttn.jobapp.Pojo.Account;
import com.ttn.jobapp.Repositories.AccountRepository;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Win11
 */
@Service
public class CustomUserDetailsService implements UserDetailsService{
    
    private AccountRepository ar;
    
    @Autowired
    public CustomUserDetailsService(AccountRepository ar){
        this.ar = ar;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = ar.findByEmail(email);
        if (account == null){
            throw new UsernameNotFoundException("Email not found");        
        }
        return new User(account.getEmail(), account.getPassword(), getAuthorities(account));
    }
    
   private Collection<GrantedAuthority> getAuthorities(Account account){
       return Collections.singleton(new SimpleGrantedAuthority(account.getRole()));
   }
    
}
