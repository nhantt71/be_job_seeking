/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;

import com.ttn.jobapp.Pojo.Company;
import jakarta.mail.MessagingException;
import java.io.IOException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Win11
 */
public interface EmailService {
    void sendEmailWithAttachment(String to, String from, String subject,
            String body, ByteArrayResource fileResource) throws MessagingException, IOException;
    
    void sendEmailWithAttachment(String to, String from, String subject, String body, MultipartFile file) throws MessagingException;
    
    void sendSimpleEmail(String to, String subject, String body);
    
    void sendVerifyEmail(String email, String token);
    
    void sendCompanyApprovalEmail(Company company);
    
    void sendCompanyRejectionEmail(Company company);
}
