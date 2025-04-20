/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Configs;

import com.ttn.jobapp.Security.JwtAuthFilter;
import com.ttn.jobapp.Repositories.AccountRepository;
import com.ttn.jobapp.ServicesImpl.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 *
 * @author Win11
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AccountRepository accountRepository;

    // 1. Use constructor injection instead of @Autowired
    public SecurityConfig(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CustomOAuth2UserService customOAuth2UserService) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**"
                ).permitAll()
                .requestMatchers("/api/auth/register", "/api/auth/verify**", "/api/auth/login", "/api/auth/check-verified**").permitAll()
                .requestMatchers("/api/candidate/create**").permitAll()
                .requestMatchers("/api/category**", "/api/job**", "/api/company**", "/api/v1/locations**").permitAll()
                .requestMatchers("/api/**").authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/login", "/oauth2/**", "/error").permitAll()
                .anyRequest().permitAll()
                )
                .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/admin", true) // Force redirect
                .failureUrl("/login?error=true")
                .permitAll()
                )
                .oauth2Login(oauth -> oauth
                .loginPage("/login")
                .defaultSuccessUrl("/admin", true)
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                )
                // Separate session policies for API vs web
                .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Changed from STATELESS
                .sessionFixation().newSession()
                )
                .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 4. Properly declare all beans
    @Bean
    public CustomOAuth2UserService customOAuth2UserService(
            AccountRepository accountRepository,
            PasswordEncoder passwordEncoder) {
        return new CustomOAuth2UserService(accountRepository, passwordEncoder);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(username -> {
            System.out.println("Attempting login for: " + username); // Debug log
            return accountRepository.findByEmail(username)
                    .map(account -> {
                        System.out.println("Found user with role: " + account.getRole()); // Debug log
                        return User.builder()
                                .username(account.getEmail())
                                .password(account.getPassword())
                                .roles(account.getRole().name())
                                .build();
                    })
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        });
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
