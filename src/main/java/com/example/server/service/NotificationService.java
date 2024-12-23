package com.example.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    @Value("${spring.mail.username}")
    private String userDetail;

    @Autowired
    JavaMailSender javaMailSender;

    @Async
    public void sendEmail(String toUser,String subject, String body){
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(userDetail);
            simpleMailMessage.setTo(toUser);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(body);
            javaMailSender.send(simpleMailMessage);
            log.info("Email sent to:{}", toUser);
        }catch (Exception e){
            log.error("Error while sending email to: {}", toUser, e);
            throw new RuntimeException("Issues in sending Mail!");
        }
    }
}
