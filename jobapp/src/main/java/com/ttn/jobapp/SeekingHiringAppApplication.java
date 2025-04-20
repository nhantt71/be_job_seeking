package com.ttn.jobapp;

import com.ttn.jobapp.Pojo.Account;
import com.ttn.jobapp.Security.JwtTokenProvider;
import com.ttn.jobapp.Services.AccountService;
import com.ttn.jobapp.Utils.AuthProvider;
import com.ttn.jobapp.Utils.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ttn.jobapp", "com.ttn.jobapp.Controllers"})
public class SeekingHiringAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeekingHiringAppApplication.class, args);
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    CommandLineRunner initAdmin(
            AccountService accountService,
            PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@example.com";

            if (!accountService.existsByEmail(adminEmail)) {
                Account admin = new Account();
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("secureAdmin123!"));
                admin.setRole(Role.ADMIN);
                admin.setVerified(true);
                admin.setProvider(AuthProvider.LOCAL);

                accountService.save(admin);
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(){
        return new JwtTokenProvider();
    }
}
