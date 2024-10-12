/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ttn.jobapp.Configs.JWTGenerator;
import com.ttn.jobapp.Dto.AccountDto;
import com.ttn.jobapp.Dto.AuthResponseDto;
import com.ttn.jobapp.Pojo.Account;
import com.ttn.jobapp.Repositories.AccountRepository;
import com.ttn.jobapp.Services.AccountService;
import com.ttn.jobapp.Utils.CloudinaryUtils;
import java.io.IOException;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Win11
 */
@RestController
@RequestMapping("api/auth")
@CrossOrigin
public class ApiAccountController {

    private AuthenticationManager authenticationManager;

    private AccountRepository accountRepository;

    private PasswordEncoder passwordEncoder;

    private JWTGenerator jwtGenerator;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private CloudinaryUtils cloudinaryUtils;

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
    public ResponseEntity<?> register(
            @RequestParam Map<String, String> params,
            @RequestPart("file") MultipartFile file) throws IOException {

        if (!params.containsKey("email") || params.get("email").isEmpty()) {
            return new ResponseEntity<>("Email is required!", HttpStatus.BAD_REQUEST);
        }

        if (!params.containsKey("password") || params.get("password").isEmpty()) {
            return new ResponseEntity<>("Password is required!", HttpStatus.BAD_REQUEST);
        }

        if (!params.containsKey("role") || params.get("role").isEmpty()) {
            return new ResponseEntity<>("Role is required!", HttpStatus.BAD_REQUEST);
        }

        if (accountRepository.existsByEmail(params.get("email"))) {
            return new ResponseEntity<>("Email is already registered!", HttpStatus.BAD_REQUEST);
        }

        if (file.isEmpty()) {
            return new ResponseEntity<>("Image file is required!", HttpStatus.BAD_REQUEST);
        }

        Account account = new Account();
        account.setEmail(params.get("email"));
        account.setPassword(passwordEncoder.encode(params.get("password")));
        account.setRole(params.get("role"));

        try {
            Map<?, ?> res = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
            account.setAvatar(res.get("secure_url").toString());
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload image.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Account savedAccount = accountRepository.save(account);

        return new ResponseEntity<>(savedAccount, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestParam Map<String, String> params) {
        if (!params.containsKey("email") || params.get("email").isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!params.containsKey("password") || params.get("password").isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(params.get("email"), params.get("password")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        String role = as.getRoleByEmail(params.get("email"));
        return new ResponseEntity<>(new AuthResponseDto(token, role), HttpStatus.OK);
    }

    @GetMapping(path = "/current-user")
    public ResponseEntity<User> getCurrentUser() {
        return new ResponseEntity<>(as.getCurrentUser(), HttpStatus.OK);
    }

    @GetMapping(path = "/get-account-by-email")
    public ResponseEntity<Account> getAccountByEmail(@RequestParam("email") String email) {
        return new ResponseEntity<>(as.getAccountByEmail(email), HttpStatus.OK);
    }


    @PostMapping("/change-password")
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
    
    @PostMapping("/change-avatar")
    public ResponseEntity<Account> changeAvatar(@RequestParam("accountId") Long accountId,
            @RequestPart MultipartFile file){
        Account account = this.as.getAccountById(accountId);
        
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Map<?, ?> res = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
            account.setAvatar(res.get("secure_url").toString());
            return new ResponseEntity<>(this.as.save(account), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
