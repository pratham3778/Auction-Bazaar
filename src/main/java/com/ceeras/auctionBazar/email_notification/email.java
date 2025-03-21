package com.ceeras.auctionBazar.email_notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

@Service
public class email {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom("87ed6a001@smtp-brevo.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true); // Set to 'true' to enable HTML content

        mailSender.send(mimeMessage);
    }
}