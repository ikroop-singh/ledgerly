package com.ledgerly.app.expense_tracker.service;


import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class EmailService {

    final private JavaMailSender javaMailSender;

    public void sendEmail(String to,String subject,String body){
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // Enable HTML content

            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("Exception occurred while sending monthly email {}", String.valueOf(e));
        }
    }
}
