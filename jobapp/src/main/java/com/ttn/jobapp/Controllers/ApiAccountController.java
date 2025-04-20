/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.LoginRequest;
import com.ttn.jobapp.Dto.LoginResponse;
import com.ttn.jobapp.Pojo.Account;
import com.ttn.jobapp.Security.JwtTokenProvider;
import com.ttn.jobapp.Services.AccountService;
import com.ttn.jobapp.Services.EmailService;
import com.ttn.jobapp.Services.SupabaseStorageService;
import com.ttn.jobapp.Utils.AuthProvider;
import com.ttn.jobapp.Utils.GenerateUniqueFileName;
import com.ttn.jobapp.Utils.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@Tag(name = "Authentication", description = "Authentication API")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin
public class ApiAccountController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public ApiAccountController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SupabaseStorageService supabaseStorageService;

    @Autowired
    private AccountService as;

    @Autowired
    private EmailService es;

    private boolean isVerified;
    private String verifyToken;

    @Autowired
    public ApiAccountController(AccountService accountService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
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

        if (accountService.existsByEmail(params.get("email"))) {
            return new ResponseEntity<>("Email is already registered!", HttpStatus.BAD_REQUEST);
        }

        if (file.isEmpty()) {
            return new ResponseEntity<>("Image file is required!", HttpStatus.BAD_REQUEST);
        }

        try {
            Account account = new Account();
            account.setEmail(params.get("email"));
            account.setPassword(passwordEncoder.encode(params.get("password")));
            account.setRole(Role.valueOf(params.get("role").toUpperCase()));
            account.setProvider(AuthProvider.LOCAL);
            String avatarUrl = supabaseStorageService.uploadFile(
                    "avatar",
                    GenerateUniqueFileName.generateUniqueFileName(file.getOriginalFilename()),
                    file.getInputStream(),
                    file.getContentType()
            );
            account.setAvatar(avatarUrl);

            Account savedAccount = new Account();

            if (params.get("role").toUpperCase().equals("CANDIDATE")) {
                String token = UUID.randomUUID().toString();
                account.setVerifyToken(token);
                account.setVerified(false);
                savedAccount = as.save(account);
                es.sendVerifyEmail(savedAccount.getEmail(), token);
            } else {
                account.setVerified(true);
                savedAccount = as.save(account);
            }

            return new ResponseEntity<>(savedAccount, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed.");
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyAccount(@RequestParam("token") String token) {
        Optional<Account> accountOpt = as.findByVerifyToken(token);

        if (accountOpt.isEmpty()) {
            // You might want to redirect to an error page or show a message
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, "http://localhost:3000/candidate/login?error=invalid_token")
                    .build();
        }

        Account account = accountOpt.get();
        account.setVerified(true);
        account.setVerifyToken(null);
        as.save(account);

        // Redirect to the login page with success message
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, "http://localhost:3000/candidate/login?verified=true")
                .build();
    }

    @Operation(
            summary = "User login",
            description = "Authenticate user and return JWT token"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        //Authenticate
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        Optional<Account> account = this.accountService.getAccountByEmail(loginRequest.getEmail());

        if (!passwordEncoder.matches(loginRequest.getPassword(), account.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid credentials"));
        }

        if (account.get().getVerified() == false) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Account not verified"));
        }

        //Set authentication in context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Generate JWT
        String jwt = jwtTokenProvider.generateToken(authentication);

        //Get user details
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse("CANDIDATE");

        //Return response
        return ResponseEntity.ok(
                new LoginResponse(
                        jwt,
                        "Bearer",
                        authentication.getName(),
                        role
                )
        );

    }

    @GetMapping(path = "/current-user")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        return new ResponseEntity<>(as.getCurrentUser(), HttpStatus.OK);
    }

    @GetMapping(path = "/get-account-by-email")
    public ResponseEntity<Optional<Account>> getAccountByEmail(@RequestParam("email") String email) {
        return new ResponseEntity<>(as.getAccountByEmail(email), HttpStatus.OK);
    }
    
    @GetMapping(path = "/check-verified")
    public ResponseEntity<Boolean> checkVerified(@RequestParam("email") String email){
        return new ResponseEntity<>(as.checkVerified(email), HttpStatus.OK);
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
            @RequestPart MultipartFile file) throws Exception {
        Account account = this.as.getAccountById(accountId);

        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            String avatarUrl = supabaseStorageService.uploadFile(
                    "avatar",
                    GenerateUniqueFileName.generateUniqueFileName(file.getOriginalFilename()),
                    file.getInputStream(),
                    file.getContentType()
            );
            account.setAvatar(avatarUrl);
            return new ResponseEntity<>(this.as.save(account), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
