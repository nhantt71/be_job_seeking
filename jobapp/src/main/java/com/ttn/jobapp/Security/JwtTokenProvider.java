/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Security;

import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Component
public class JwtTokenProvider {

    protected final String SECRET_KEY = "52350425T3U494G34G43NG43G934G43GM3G3G24GGDFGDGDFGDFGDFG4G2GFGDFGCGBBBVKVJK3551131GSYGS789GYS98NDF9JNDG6N0DNHDHHGFHFGH";
    protected final long EXPIRATION_MS = 7200000; // 2 hours

    public String generateToken(Authentication auth) {
        String role = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_CANDIDATE");

        return Jwts.builder()
                .setSubject(auth.getName())
                .claim("role", role) // Embed role in JWT
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .compact();
    }

    public String getUsernameFromAuth(Authentication auth) {
        if (auth.getPrincipal() instanceof OAuth2User oauthUser) {
            return oauthUser.getAttribute("email");
        } else if (auth.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return auth.getName();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Log specific exceptions (expired, malformed, etc.)
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        UserDetails userDetails = User.builder()
                .username(claims.getSubject())
                .password("") // Password not needed for token auth
                .authorities("ROLE_" + "") // Or extract from claims
                .build();

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
