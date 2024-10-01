/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Configs.JWTGenerator;
import com.ttn.jobapp.Dto.AccountDto;
import com.ttn.jobapp.Dto.AuthResponseDto;
import com.ttn.jobapp.Pojo.Account;
import com.ttn.jobapp.Repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Win11
 */
@RestController
@RequestMapping("api/auth")
public class ApiAccountController {
    
    private AuthenticationManager authenticationManager;
    
    private AccountRepository accountRepository;
    
    private PasswordEncoder passwordEncoder;
    
    private JWTGenerator jwtGenerator;
    
    @Autowired
    public ApiAccountController(AuthenticationManager authenticationManager, AccountRepository accountRepository,
            PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator){
        this.authenticationManager = authenticationManager;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AccountDto accountDto){
        if(accountRepository.existsByEmail(accountDto.getEmail())){
            return new ResponseEntity<>("Email is existed!", HttpStatus.BAD_REQUEST);
        }
        
        Account account = new Account();
        account.setEmail(accountDto.getEmail());
        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        account.setAvailable(Boolean.FALSE);
        account.setAvatar("");
        account.setRole("employee");
        
        accountRepository.save(account);
        
        return new ResponseEntity<>("Account registered success!", HttpStatus.OK);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AccountDto accountDto){
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(accountDto.getEmail(), accountDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }
}
