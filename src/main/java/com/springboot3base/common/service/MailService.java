package com.springboot3base.common.service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {
    private final JavaMailSender javaMailSender;

    @Value("${web.mail.smtp.mail}")
    String from;

    public boolean sendMail(String to, String subject, String content) {
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.setSubject(subject, "utf-8");
            mimeMessage.setText(content, "utf-8", "html");
        };

        try {
            javaMailSender.send(preparator);
            return true;
        } catch (MailException me) {
            log.error("MailException", me);
            return false;
        }
    }
}
