package com.priceComparison.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class Email {

    private JavaMailSender javaMailSender;


    public void sendVerificationEmail(String toEmail,String token){

        String verificationUrl="http://localhost:8080/verify-email?token=" + token;
        SimpleMailMessage mailmessage = new SimpleMailMessage();
        mailmessage.setTo(toEmail);
        mailmessage.setSubject("Account verification ");
        mailmessage.setText("verify your account by following link");
        javaMailSender.send(mailmessage);
    }
}
