package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Component
public class EmailUtil {

    @Autowired
    public JavaMailSender emailSender;

    public void sendSimpleMessage(String recipient, String emailSubject,
                                  String emailBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject(emailSubject);
        message.setText(emailBody);
//        message.setFrom(DemoConstants.EMAIL_FROM);
        emailSender.send(message);
    }

    public void sendMessageWithAttachment(String recipient, String emailSubject,
                                          String emailBody, String pathToAttachment)
            throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipient);
        helper.setSubject(emailSubject);
        helper.setText(emailBody);

        FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
        helper.addAttachment("Invoice", file);

        emailSender.send(message);
    }

}
