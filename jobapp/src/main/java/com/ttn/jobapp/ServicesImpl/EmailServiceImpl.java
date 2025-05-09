/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Pojo.Company;
import com.ttn.jobapp.Services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 *
 * @author Win11
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void sendEmailWithAttachment(String to, String from, String subject,
            String body, ByteArrayResource fileResource)
            throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setFrom(from);
        helper.setSubject(subject);
        helper.setText(body);

        String fileName = fileResource.getFilename();
        helper.addAttachment(fileName, fileResource);

        mailSender.send(message);
    }

    @Override
    public void sendEmailWithAttachment(String to, String from, String subject, String body, MultipartFile file) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setFrom(from);
        helper.setSubject(subject);
        helper.setText(body, true);

        String fileName = file.getOriginalFilename();
        helper.addAttachment(fileName, file);

        mailSender.send(message);
    }

    @Override
    public void sendSimpleEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("tonhanlk113@gmail.com");
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    @Override
    public void sendVerifyEmail(String email, String token) {
        String subject = "Xác minh tài khoản của bạn";
        String verifyLink = "http://localhost:8080/api/auth/verify?token=" + token;

        String body = "Xin chào,\n\n"
                + "Cảm ơn bạn đã đăng ký tài khoản.\n"
                + "Vui lòng nhấp vào liên kết sau để xác minh tài khoản của bạn:\n"
                + verifyLink + "\n\n"
                + "Trân trọng,\nHệ thống tuyển dụng thông minh.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("tonhanlk113@gmail.com");
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    @Override
    public void sendCompanyApprovalEmail(Company company) {
        Context context = new Context();
        context.setVariable("company", company);

        String content = templateEngine.process("email/company-approved", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(company.getEmail());
            helper.setSubject("Your Company Has Been Approved");
            helper.setText(content, true);
        } catch (MessagingException ex) {
            Logger.getLogger(EmailServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        mailSender.send(message);
    }

    @Override
    public void sendCompanyRejectionEmail(Company company) {
                Context context = new Context();
        context.setVariable("company", company);

        String content = templateEngine.process("email/company-rejected", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(company.getEmail());
            helper.setSubject("Your Company Has Been Rejected");
            helper.setText(content, true);
        } catch (MessagingException ex) {
            Logger.getLogger(EmailServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        mailSender.send(message);
    }

}
