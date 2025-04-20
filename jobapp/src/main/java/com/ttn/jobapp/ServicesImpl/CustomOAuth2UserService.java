/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Pojo.Account;
import com.ttn.jobapp.Repositories.AccountRepository;
import com.ttn.jobapp.Services.SupabaseStorageService;
import com.ttn.jobapp.Utils.AuthProvider;
import com.ttn.jobapp.Utils.Role;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private final PasswordEncoder passwordEncoder;
    
    public CustomOAuth2UserService(AccountRepository accountRepository, PasswordEncoder passwordEncoder){
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Autowired
    private SupabaseStorageService supabaseStorageService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Extract common attributes
        String email = oAuth2User.getAttribute("email");
        String provider = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        String avatarUrl = (String) Optional.ofNullable(oAuth2User.getAttribute("picture"))
                .orElse(oAuth2User.getAttribute("avatar_url"));

        // Find or create account
        Account account = accountRepository.findByEmail(email)
                .orElseGet(() -> createNewAccount(email, provider, avatarUrl));

        // Update existing account's avatar if needed
        if (account.getAvatar() == null || !account.getAvatar().equals(avatarUrl)) {
            updateAccountAvatar(account, avatarUrl);
        }
        
        Role role = determineRole(email);

        return new DefaultOAuth2User(
                Set.of(new SimpleGrantedAuthority("ROLE_" + role.name())),
                oAuth2User.getAttributes(),
                "email");
    }

    private Role determineRole(String email) {
        return Role.valueOf(this.accountRepository.getRoleByEmail(email));
    }
    
    private Account createNewAccount(String email, String provider, String avatarUrl) {
        Account newAccount = new Account();
        newAccount.setEmail(email);
        newAccount.setRole(Role.CANDIDATE);
        newAccount.setVerified(true);
        newAccount.setProvider(AuthProvider.valueOf(provider));
        newAccount.setAvatar(processAvatar(avatarUrl));
        return accountRepository.save(newAccount);
    }

    private void updateAccountAvatar(Account account, String avatarUrl) {
        String processedAvatar = processAvatar(avatarUrl);
        account.setAvatar(processedAvatar);
        accountRepository.save(account);
    }

    private String processAvatar(String avatarUrl) {
        if (avatarUrl == null) {
            return null;
        }

        try {
            URL url = new URL(avatarUrl);
            try (InputStream inputStream = url.openStream()) {
                String filename = UUID.randomUUID() + ".jpg";
                return supabaseStorageService.uploadFile(
                        "avatar",
                        filename,
                        inputStream,
                        "image/jpeg"
                );
            }
        } catch (IOException e) {
            Logger.getLogger(CustomOAuth2UserService.class.getName())
                    .log(Level.WARNING, "Failed to upload avatar, using original URL", e);
            return avatarUrl;
        } catch (Exception ex) {
            Logger.getLogger(CustomOAuth2UserService.class.getName())
                    .log(Level.SEVERE, "Unexpected error processing avatar", ex);
            return avatarUrl;
        }
    }

}
