package com.bookhub.Service;

import com.bookhub.CustomException.EmailException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmail(String to, String subject, String text) throws MessagingException {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("aungthuhein.bm@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        } catch (MailException e) {
            throw new EmailException("Failed to send email to " + to + ": " + e.getMessage());
        }
    }
}
