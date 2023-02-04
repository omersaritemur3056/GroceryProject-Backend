package com.example.grocery.core.mailing.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.grocery.core.mailing.helper.ValidationCodeGenerator;
import com.example.grocery.core.mailing.model.Email;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessResult;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String senderMail;

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public Result sendActivationEmail(String email) {
        Email mail = new Email();
        mail.setEmailFrom(senderMail);
        mail.setEmailTo(email);
        mail.setSubject("Grocery - Validation Code");
        mail.setContent(
                "Hello, \n\n Your verification code: " + ValidationCodeGenerator.generateSixDigitRandomNumber());

        sendSimpleMessage(mail);
        log.info("Activation mail send!");
        return new SuccessResult("Activation mail send!");
    }

    @Override
    public void sendSimpleMessage(Email email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(email.getSubject());
        message.setText(email.getContent());
        message.setTo(email.getEmailTo());
        message.setFrom(email.getEmailFrom());

        javaMailSender.send(message);

    }

    // designed for advert message
    @Override
    public void sendEmail(String email) {
        Email mail = new Email();
        mail.setEmailFrom(senderMail);
        mail.setEmailTo(email);
        mail.setSubject("Grocery - Newsletter");
        mail.setContent("Override this...");

        sendSimpleMessage(mail);
    }

}
