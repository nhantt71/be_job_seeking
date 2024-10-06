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
import com.ttn.jobapp.Services.AccountService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    private AccountService as;

    @Autowired
    public ApiAccountController(AuthenticationManager authenticationManager, AccountRepository accountRepository,
            PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AccountDto accountDto) {
        if (accountRepository.existsByEmail(accountDto.getEmail())) {
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
    public ResponseEntity<AuthResponseDto> login(@RequestBody AccountDto accountDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(accountDto.getEmail(), accountDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }

    @GetMapping(path = "/current-user")
    public User getCurrentUser() {
        return as.getCurrentUser();
    }

    @GetMapping(path = "/get-account-by-email")
    public ResponseEntity<Account> getAccountByEmail(@RequestParam("email") String email) {
        return new ResponseEntity<>(as.getAccountByEmail(email), HttpStatus.OK);
    }

    @PutMapping("/enable-finding-for-job")
    public ResponseEntity<Account> enableFindingForJob(@RequestParam("accountId") Long accountId) {
        Account account = this.as.getAccountById(accountId);

        account.setAvailable(Boolean.TRUE);

        this.as.save(account);

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PutMapping("/disable-finding-for-job")
    public ResponseEntity<Account> disableFindingForJob(@RequestParam("accountId") Long accountId) {
        Account account = this.as.getAccountById(accountId);

        account.setAvailable(Boolean.FALSE);

        this.as.save(account);

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<Account> changePassword(@RequestParam("accountId") Long accountId,
            @RequestParam("newPassword") String newPassword, @RequestParam("oldPassword") String oldPassword) {

        Account account = this.as.getAccountById(accountId);

        if (passwordEncoder.matches(oldPassword, account.getPassword())) {
            account.setPassword(passwordEncoder.encode(newPassword));
            this.as.save(account);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
}
