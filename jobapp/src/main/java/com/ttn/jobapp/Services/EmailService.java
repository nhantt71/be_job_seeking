/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;

import jakarta.mail.MessagingException;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Win11
 */
public interface EmailService {
    void sendEmailWithAttachment(String to, String from, String subject,
            String body, MultipartFile file) throws MessagingException, IOException;
    
    void sendSimpleEmail(String to, String subject, String body);
}
